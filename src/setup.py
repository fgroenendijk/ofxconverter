from cx_Freeze import setup, Executable

import sys
base = None
icon = None
if sys.platform == u'win32':
    base = u'Win32GUI'
    icon = u'../images/chameleon-icon.png'

executables = [
    Executable(u"main.py",
               icon=icon,
               appendScriptToExe=True,
               appendScriptToLibrary=False,
               base=base,
               targetName = u'ofxconverter'
               )
]

buildOptions = dict(create_shared_zip=False)

setup(name=u'OFXConverter',
      version = u'1.0',
      description = u'Convert csv to ofx file',
      options = dict(build_exe = buildOptions),
      executables = executables)
