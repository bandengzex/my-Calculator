# 科学计算器安卓应用

一款功能完整的科学计算器安卓应用，采用卡西欧经典设计风格，支持自然书写格式的数学表达式显示。

## 功能特性

### 1. 自然书写格式显示
- 使用MathView库实现LaTeX数学公式渲染
- 支持分数、根号、指数等数学符号的自然书写格式
- 表达式排版贴近手写习惯，直观清晰

### 2. 卡西欧风格键盘布局
- 参考卡西欧科学计算器的经典配置
- 6×6按键布局，功能分区明确
- 支持长按操作和快捷键

### 3. 完整的科学计算功能

#### 基本运算
- 加减乘除四则运算
- 小数点运算
- 正负号切换
- 百分比计算
- 倒数计算

#### 高级函数
- **三角函数**: sin, cos, tan
- **反三角函数**: asin, acos, atan
- **对数函数**: log(常用对数), ln(自然对数)
- **指数函数**: x², x³, x^y
- **根式函数**: √x
- **阶乘**: n!

#### 数学常数
- π (圆周率)
- e (自然常数)

#### 角度单位
- 度 (°)
- 分 (′)
- 秒 (″)

### 4. 用户界面特性
- 清晰的显示屏，分为表达式显示区和结果显示区
- 按键采用不同颜色区分功能类型
- 支持表达式编辑和删除
- 错误提示功能

## 技术实现

### 开发环境
- **编程语言**: Java
- **开发工具**: Android Studio
- **最低API版本**: 24 (Android 7.0)
- **目标API版本**: 34 (Android 14)

### 核心技术栈
- **UI框架**: Android原生UI组件
- **数学表达式渲染**: MathView库 (LaTeX)
- **数学计算引擎**: Rhino JavaScript引擎
- **布局管理**: ConstraintLayout + LinearLayout

### 项目结构
```
Scientific Calculator/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/scientificcalculator/
│   │   │   │   └── MainActivity.java          # 主活动类
│   │   │   ├── res/
│   │   │   │   ├── layout/
│   │   │   │   │   └── activity_main.xml     # 主界面布局
│   │   │   │   ├── values/
│   │   │   │   │   ├── strings.xml           # 字符串资源
│   │   │   │   │   ├── colors.xml            # 颜色资源
│   │   │   │   │   └── styles.xml            # 样式资源
│   │   │   │   └── drawable/
│   │   │   │       └── button_background.xml  # 按钮背景
│   │   │   └── AndroidManifest.xml          # 应用配置
│   │   └── test/
│   └── build.gradle                          # 应用级构建配置
├── build.gradle                                # 项目级构建配置
├── settings.gradle                            # 项目设置
├── gradle.properties                          # Gradle属性
└── README.md                                  # 项目说明
```

## 安装和运行

### 环境要求
- Android Studio 2022.1.1 或更高版本
- JDK 8 或更高版本
- Android SDK API 24-34

### 构建步骤
1. 克隆或下载项目代码
2. 在Android Studio中打开项目
3. 等待Gradle同步完成
4. 连接Android设备或启动模拟器
5. 点击运行按钮构建并安装应用

### 依赖库
- MathView: 用于LaTeX数学公式渲染
- Rhino: JavaScript引擎，用于数学计算
- AndroidX: Android支持库
- Material Components: Material Design组件

## 使用说明

### 基本操作
1. **数字输入**: 点击数字键0-9输入数字
2. **小数点**: 点击「.」键输入小数点
3. **基本运算**: 使用 +、−、×、÷ 进行四则运算
4. **等号**: 点击「=」键计算结果
5. **清除**: 
   - 「C」清除当前输入
   - 「AC」清除所有内容
   - 「⌫」删除最后一个字符

### 高级功能
1. **三角函数**: 点击sin、cos、tan键，然后输入角度值
2. **反三角函数**: 点击sin⁻¹、cos⁻¹、tan⁻¹键
3. **对数函数**: 点击log(常用对数)或ln(自然对数)键
4. **指数运算**: 
   - 点击x²键计算平方
   - 点击x³键计算立方
   - 点击x^y键，然后输入底数和指数
5. **根式运算**: 点击√键，然后输入被开方数
6. **阶乘**: 输入数字后点击n!键
7. **常数**: 点击π或e键输入相应常数

### 表达式编辑
- 支持括号改变运算优先级
- 可以编辑和修改已输入的表达式
- 实时显示LaTeX格式的数学表达式

## 开发说明

### 核心类说明

#### MainActivity
- **功能**: 主活动类，处理用户交互和计算逻辑
- **关键方法**:
  - `onClick()`: 处理按钮点击事件
  - `calculateResult()`: 执行数学计算
  - `updateExpressionDisplay()`: 更新表达式显示
  - `convertToLatex()`: 转换为LaTeX格式
  - `convertToJavaScript()`: 转换为JavaScript表达式

### 关键技术点

#### 1. LaTeX表达式渲染
使用MathView库实现数学表达式的自然书写格式显示：
```java
MathView expressionDisplay = findViewById(R.id.expression_display);
expressionDisplay.setText("$$" + latexExpression + "$$");
```

#### 2. JavaScript计算引擎
使用Rhino引擎执行复杂数学计算：
```java
Context jsContext = Context.enter();
Scriptable jsScope = jsContext.initStandardObjects();
Object result = jsContext.evaluateString(jsScope, jsExpression, "<cmd>", 1, null);
```

#### 3. 表达式转换
实现用户界面表达式到JavaScript表达式的转换：
```java
private String convertToJavaScript(String expression) {
    return expression.replace(" × ", " * ")
                   .replace(" ÷ ", " / ")
                   .replace("√(", "sqrt(")
                   .replace("²", "**2")
                   .replace("³", "**3")
                   .replace("π", "PI")
                   .replace("e", "E");
}
```

## 扩展功能建议

### 可以添加的功能
1. **历史记录**: 保存计算历史，支持查看和重用
2. **单位转换**: 长度、重量、温度等单位转换
3. **矩阵运算**: 矩阵加减乘除、求逆、行列式等
4. **复数运算**: 复数的基本运算和函数
5. **方程求解**: 一元二次方程、线性方程组求解
6. **绘图功能**: 函数图像绘制
7. **主题切换**: 多种颜色主题选择
8. **语音输入**: 支持语音输入数学表达式

### 性能优化
1. **表达式缓存**: 缓存常用表达式计算结果
2. **异步计算**: 复杂计算使用异步任务避免界面卡顿
3. **内存优化**: 优化大数计算和复杂表达式的内存使用

## 许可证
本项目采用MIT许可证，详见LICENSE文件。

## 贡献指南
欢迎提交Issue和Pull Request来改进这个项目。

## 联系方式
如有问题或建议，请通过以下方式联系：
- 邮箱: [your-email@example.com]
- GitHub: [your-github-username]

---

**注意**: 这是一个教育示例项目，在生产环境中使用时请进行充分的测试和优化。