ren "C:\WINDOWS\inf\usbstop.inf" "usbstor.inf"
ren "C:\WINDOWS\inf\usbstop.PNF" "usbstor.PNF"
reg add "HKEY_LOCAL_MACHINE\SYSTEM\CurrentControlSet\Services\USBSTOR" /v Start /t reg_dword /d 3 /f