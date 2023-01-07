@echo off&color 17
if exist "%SystemRoot%\SysWOW64" path %path%;%windir%\SysNative;%SystemRoot%\SysWOW64;%~dp0
bcdedit >nul
if '%errorlevel%' NEQ '0' (goto UACPrompt) else (goto UACAdmin)
:UACPrompt
%1 start "" mshta vbscript:createobject("shell.application").shellexecute("""%~0""","::",,"runas",1)(window.close)&exit
exit /B
:UACAdmin
cd /d "%~dp0"
echo 当前运行路径是：%CD%
echo 已获取管理员权限
ren "C:\WINDOWS\inf\usbstor.inf" "usbstop.inf"
ren "C:\WINDOWS\inf\usbstor.PNF" "usbstop.PNF"
reg add "HKEY_LOCAL_MACHINE\SYSTEM\CurrentControlSet\Services\USBSTOR" /v Start /t reg_dword /d 4 /f
