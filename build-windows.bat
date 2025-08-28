@echo off
REM 科学计算器项目构建脚本（Windows版本）

echo 请选择构建类型：
echo 1. Debug构建

echo 2. Release构建
set /p build_choice=请输入选项 [1-2]: 

if "%build_choice%"=="1" (
    set build_type=Debug
) else if "%build_choice%"=="2" (
    set build_type=Release
) else (
    echo 无效的选项，默认使用Debug构建
    set build_type=Debug
)

echo.
echo 开始%build_type%构建...
echo.

REM 确保gradlew有执行权限
if not exist "gradlew" (
    echo 错误：找不到gradlew文件。
    pause
    exit /b 1
)

REM 执行构建
call gradlew assemble%build_type%

if %errorlevel% neq 0 (
    echo.
echo 构建失败！
    pause
    exit /b 1
)

echo.
echo 构建成功！
echo 构建产物位置：app\build\outputs\apk\%build_type:~0,1%%build_type:~1%\app-%build_type:~0,1%%build_type:~1%.apk

echo.
echo 是否运行测试？(y/n): 
set /p run_tests=

if /i "%run_tests%"=="y" (
    echo 运行测试...
    call gradlew test
    
    if %errorlevel% neq 0 (
        echo 测试失败！
    ) else (
        echo 测试通过！
    )
)

pause