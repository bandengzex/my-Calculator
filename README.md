# 科学计算器安卓应用

一款功能强大的科学计算器安卓应用，支持自然书写格式的数学表达式显示和卡西欧风格的键盘布局。

## 功能特性

### 核心功能
- **自然书写格式**：数学表达式以手写习惯方式显示，包括分数、根号、指数等
- **卡西欧风格键盘**：经典布局和交互逻辑，提供熟悉的用户体验
- **科学计算**：支持sin、cos、tan、log、ln、√等科学函数
- **实时计算**：输入表达式时实时显示计算结果

### 支持的运算
- 基本运算：加(+)、减(−)、乘(×)、除(÷)
- 科学函数：sin、cos、tan、log、ln、√
- 指数运算：幂运算(^)
- 括号运算：支持多层嵌套括号
- 常数：π(pi)和e(自然对数底数)

### 键盘布局
采用卡西欧科学计算器经典布局：
- 5列8行的网格布局
- 功能键区域（顶部）
- 数字键区域（中央）
- 运算符键区域（右侧）
- 控制键区域（底部）

## 技术架构

### 构建方式
- **GitHub Actions**：完全使用GitHub进行构建，无需Android Studio
- **Gradle构建**：使用Gradle 8.0进行项目构建
- **Java 8**：基于Java 8开发

### 项目结构
```
app/
├── src/main/java/com/scientific/calculator/
│   ├── MainActivity.java          # 主界面活动
│   └── MathExpressionEvaluator.java # 数学表达式求值器
├── src/main/res/
│   ├── layout/activity_main.xml   # 主界面布局
│   ├── values/                   # 资源文件
│   └── mipmap-*/                 # 图标资源
└── build.gradle                  # 应用构建配置
```

## 使用方法

### 本地开发
1. 克隆项目到本地
2. 确保已安装Java 11或更高版本
3. 运行以下命令构建项目：
   ```bash
   ./gradlew build
   ```

### GitHub构建
项目已配置GitHub Actions，每次推送代码到main分支时自动构建：
- 自动运行单元测试
- 生成调试APK
- 发布版本APK（需要配置签名密钥）

### 安装APK
1. 从GitHub Actions下载构建的APK文件
2. 在安卓设备上允许安装未知来源应用
3. 安装下载的APK文件

## 开发说明

### 环境要求
- Android SDK 34
- Java 11或更高版本
- Gradle 8.0

### 构建命令
```bash
# 构建调试版本
./gradlew assembleDebug

# 构建发布版本
./gradlew assembleRelease

# 运行测试
./gradlew test

# 清理项目
./gradlew clean
```

### 自定义配置
- 修改`app/build.gradle`调整构建配置
- 编辑`app/src/main/res/layout/activity_main.xml`自定义界面
- 更新`app/src/main/java/com/scientific/calculator/`下的Java文件添加新功能

## 贡献指南

欢迎提交Issue和Pull Request来改进这个科学计算器应用。

### 开发流程
1. Fork项目
2. 创建功能分支
3. 提交更改
4. 创建Pull Request

## 许可证

本项目采用MIT许可证，详见[LICENSE](LICENSE)文件。