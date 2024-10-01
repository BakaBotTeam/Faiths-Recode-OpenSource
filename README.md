# Faiths-Recode-OpenSource
## Faiths-Recode 开发环境部署指南
* 使用Intellij IDEA打开本项目, 导入Maven Project
* 创建运行配置 
    * 运行类 net.minecraft.client.main.Main 
    * 添加运行前任务 选择Maven Goal, 任务内容: `dependency:unpack-dependencies -Dmdep.unpack.includes=**/*.dll,**/*.so,**/*.jnilib,**/*.dylib`
    * 添加运行Jvm参数 内容: `-Dphantom-shield-x.cloud-constant.2066960.0=1857748011 -Dphantom-shield-x.cloud-constant.-1808631973.0=-1521957196 -Dorg.lwjgl.librarypath=$MODULE_DIR$/target/dependency`
    * 添加程序运行参数 内容: `--version mcp --accessToken 0 --assetsDir assets --assetIndex 1.8 --userProperties {}`
