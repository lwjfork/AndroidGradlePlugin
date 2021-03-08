package com.lwjfork.android.gradle.aop.analyzer

import com.android.build.api.transform.DirectoryInput
import com.android.build.api.transform.Format
import com.android.build.api.transform.JarInput
import com.android.build.api.transform.TransformOutputProvider
import com.android.build.gradle.AppExtension
import com.lwjfork.android.gradle.aop.analyzer.dir.SimpleDirectoryAnalyzer
import com.lwjfork.android.gradle.aop.analyzer.jar.SimpleJarAnalyzer
import com.lwjfork.android.gradle.aop.javassist.ClassPath
import com.lwjfork.android.gradle.aop.model.DirInputInfo
import com.lwjfork.android.gradle.aop.model.JarInputInfo
import com.lwjfork.aop.analyzer.AnalyzerExecutor
import com.lwjfork.aop.analyzer.exector.IAnalyzer
import com.lwjfork.aop.collector.ClassCollectorExecutor
import com.lwjfork.aop.collector.model.CollectorResult
import com.lwjfork.aop.packetizer.PacketizerExecutor
import com.lwjfork.gradle.adapter.model.VariantInfoModel
import com.lwjfork.gradle.adapter.util.PathAdapterUtil
import javassist.ClassPool
import org.gradle.api.Project

class CommonAnalyzer {

    static final EXPLODED_AAR = 'exploded-aar'
    protected Project project
    protected VariantInfoModel variantInfoModel
    protected TransformOutputProvider outputProvider
    public ClassPool classPool
    protected ClassPath classPath
    protected ArrayList<DirInputInfo> dirInputInfos = new ArrayList<>()
    protected ArrayList<JarInputInfo> jarInputInfos = new ArrayList<>()
    protected String explodedJar
    protected AnalyzerExecutor analyzerExecutor
    protected AppExtension appExtension
    private ArrayList<IAnalyzer<Void>> analyzers = new ArrayList<>()
    /**
     * 目录遍历分析器
     */
    private ArrayList<SimpleDirectoryAnalyzer> directoryAnalyzers = new ArrayList<>()
    /**
     * jar 包遍历分析器(解压过得)
     */
    private ArrayList<SimpleJarAnalyzer> jarAnalyzers = new ArrayList<>()

    CommonAnalyzer(Project project, AppExtension appExtension, VariantInfoModel variantInfoModel, TransformOutputProvider outputProvider) {
        this.project = project
        this.variantInfoModel = variantInfoModel
        this.outputProvider = outputProvider
        this.appExtension = appExtension
        this.explodedJar = PathAdapterUtil.getIntermediatesSubDir(project, EXPLODED_AAR, variantInfoModel)
        this.classPath = new ClassPath(project, this.appExtension)
    }

    void execute() {
        this.initClassPool()
        // 1 收集
        CollectorResult collectorResult = this.collectClasses()
        // 2 分析
        this.initAnalyzerExecutor(collectorResult)
        // 执行分析，插入代码
        this.analyzerExecutor.analyze()
        // 3 打包
        packageFile(collectorResult)
    }


    protected void initAnalyzerExecutor(CollectorResult collectorResult){
        this.analyzerExecutor = new AnalyzerExecutor(collectorResult)
        analyzers.forEach{
            this.analyzerExecutor.addAnalyzer(it)
        }
        this.jarAnalyzers.forEach{
            it.setClassPool(this.classPool)
            this.analyzerExecutor.addJarAnalyzer(it)
        }
        this.directoryAnalyzers.forEach{
            it.setClassPool(this.classPool)
            this.analyzerExecutor.addDirectoryAnalyzer(it)
        }
    }

    def addJarAnalyzer(SimpleJarAnalyzer jarAnalyzer) {
        this.jarAnalyzers.add(jarAnalyzer)
    }

    def addDirectoryAnalyzer(SimpleDirectoryAnalyzer directoryAnalyzer) {
        this.directoryAnalyzers.add(directoryAnalyzer)
    }

    def addAnalyzer(IAnalyzer<Void> analyzer) {
        this.analyzers.add(analyzer)
    }


    /**
     * 收集文件
     * @param collectorExecutor
     * @param dirInputInfos
     * @param jarInputInfos
     * @return
     */
    protected CollectorResult collectClasses() {
        ClassCollectorExecutor collectorExecutor = new ClassCollectorExecutor()
        this.dirInputInfos.each { dirInputInfo ->
            collectorExecutor.addDirectory(dirInputInfo.sourcePath, dirInputInfo.destPath, "", true)
        }
        this.jarInputInfos.each { jarInputInfo ->
            collectorExecutor.addJar(jarInputInfo.sourcePath, jarInputInfo.unzipPath, jarInputInfo.getDestPath())
        }
        return collectorExecutor.collect()
    }

    /**
     * 初始化 classPool
     * @param classPath
     */
    protected def initClassPool() {
        classPool = new ClassPool(true)
        classPool.insertClassPath(classPath.getAndroidJarPath())
        classPath.getDirPath().each { it ->
            classPool.insertClassPath(it)
        }
        classPath.getUnzipJarPath().each { it ->
            classPool.insertClassPath(it)
        }
    }

    void addDirectoryInput(DirectoryInput directoryInput) {
        DirInputInfo dirInputInfo = this.initDirInputExecutor(directoryInput)
        classPath.addDirPath(dirInputInfo.sourcePath)
        dirInputInfos.add(dirInputInfo)
    }

    void addJarInput(JarInput jarInput) {
        JarInputInfo jarInputInfo = this.initJarInputExecutor(jarInput)
        classPath.addUnzipJarPath(jarInputInfo.unzipPath)
        jarInputInfos.add(jarInputInfo)
    }


    /**
     *  处理 dir
     * @param directoryInput
     * @param outputProvider
     * @param collectorExecutor
     * @param classPath
     * @param packetizerExecutor
     */
    protected DirInputInfo initDirInputExecutor(DirectoryInput directoryInput) {
        DirInputInfo dirInputInfo = new DirInputInfo()
        dirInputInfo.sourcePath = directoryInput.file.absolutePath
        File destFile = this.outputProvider.getContentLocation(directoryInput.name, directoryInput.contentTypes, directoryInput.scopes, Format.DIRECTORY)
        dirInputInfo.destPath = destFile.absolutePath
        return dirInputInfo
    }

    /**
     *  处理 jar
     * @param jarInput
     * @param outputProvider
     * @param explodedJar
     * @param classPath
     * @param collectorExecutor
     * @param packetizerExecutor
     */
    protected JarInputInfo initJarInputExecutor(JarInput jarInput) {
        JarInputInfo jarInputInfo = new JarInputInfo()
        jarInputInfo.sourcePath = jarInput.file.absolutePath
        // 拷贝后的路径
        File destFile = this.outputProvider.getContentLocation(jarInput.name, jarInput.contentTypes, jarInput.scopes, Format.JAR)
        jarInputInfo.destPath = destFile.absolutePath
        // 生成jar 的解压路径
        if (jarInputInfo.sourcePath.startsWith(this.project.getRootDir().absolutePath)) {
            // module 中的 直接解压到项目中
            jarInputInfo.unzipPath = jarInput.file.getParent() + jarInput.file.getName().replace(".jar", "")
        } else {
            // 远程 aar 或者 jar 依赖，则将其复制解压到项目目录中
            if (jarInput.file.getName() == "classes.jar") {
                if (jarInput.file.parentFile.getName() == "jars") {
                    String realName = jarInput.file.parentFile.parentFile.getName()
                    jarInputInfo.unzipPath = this.explodedJar + File.separator + realName + "-classes"
                } else {
                    jarInputInfo.unzipPath = this.explodedJar + File.separator + jarInput.file.parentFile.getName() + "-classes"
                }
            } else {
                jarInputInfo.unzipPath = this.explodedJar + File.separator + jarInput.file.getName().replace(".jar", "")
            }
        }
        return jarInputInfo
    }

    /**
     * 打包
     */
    protected void packageFile(CollectorResult files) {
        PacketizerExecutor packetizerExecutor = new PacketizerExecutor(true)
        files.jars.each { compileJarModel ->
            packetizerExecutor.addJar(compileJarModel.sourcePath, compileJarModel.unzipDirPath, compileJarModel.destPath)
        }
        files.dirs.each { compileDirModel ->
            packetizerExecutor.addDirectory(compileDirModel.sourcePath, compileDirModel.destPath)
        }
        packetizerExecutor.packet()
    }
}
