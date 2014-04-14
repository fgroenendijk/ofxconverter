from configobj import ConfigObj
from os.path import expanduser
from os.path import join
from os.path import isdir
from os.path import isfile
from os.path import exists
from os import makedirs
import errno

class Config:

    # Numeric order config file
    # 1 interestDate
    # 2 date
    # 3 amount
    # 4 memo
    # 5 name
    # 6 account
    # 7 type

    def writeNewConfig(self,filename):
        config = [
            'NL94INGB = 1,2,3,4,5,6,7',
            'NL95INGB = 1,2,3,4,5,6,7'
            ]            

        print( config )

        cfg = ConfigObj( config )
        print( cfg )
        cfg.filename = filename
        cfg.write()

    def checkForConfig(self):
        home = expanduser("~")
        configDir = join( home, '.ofxconverter' )

        try:
            makedirs( configDir )
        except OSError as exception:
            if exception.errno != errno.EEXIST:
                raise            
        
        configFile = join( configDir, 'banks.config' )
        
        if not exists( configFile ) or isfile( configFile ):
            print( 'Write new config file:', configFile )
            self.writeNewConfig( configFile )
            

if __name__ == '__main__':
    config = Config()
    config.checkForConfig()
