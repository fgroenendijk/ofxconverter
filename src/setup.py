from cx_Freeze import setup, Executable

import sys
base = None
icon = None
if sys.platform == 'win32':
    base = 'Win32GUI'
    icon = '../images/chameleon-icon.png'

executables = [
    Executable("main.py",
               icon=icon,
               appendScriptToExe=True,
               appendScriptToLibrary=False,
               base=base,
               targetName = 'ofxconverter'
               )
]

buildOptions = dict(create_shared_zip=False)

setup(name='OFXConverter',
      version = '1.0',
      description = 'Convert csv to ofx file',
      options = dict(build_exe = buildOptions),
      executables = executables)
