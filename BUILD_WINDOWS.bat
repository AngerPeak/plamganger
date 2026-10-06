@echo off
chcp 65001 >nul
echo === Сборка мода PALM GANGER (нужен интернет, первый раз ~5-10 минут) ===
call gradlew.bat build
if errorlevel 1 (
  echo.
  echo ОШИБКА СБОРКИ. Пришли мне текст выше - я исправлю.
  pause
  exit /b 1
)
if not exist OUTPUT mkdir OUTPUT
copy /Y build\libs\palmganger-*.jar OUTPUT\ >nul
echo.
echo ГОТОВО! Файл мода лежит в папке OUTPUT. Скопируй его в .minecraft\mods
start "" OUTPUT
pause
