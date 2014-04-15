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
    # 8 debet/credit

    def writeNewConfig(self,filename):
        config = [
            'NL94INGB = 1,1,7,9,2,3,5,6',
            'NL95INGB = 1,2,3,4,5,6,7,8'
            ]            

        cfg = ConfigObj( config )
        cfg.filename = filename
        cfg.write()

    def checkForConfig(self):
        home = expanduser("~")
        configDir = join( home, '.ofxconverter' )

        makedirs( configDir, exist_ok=True )       
        self.configFile = join( configDir, 'banks.config' )
        
        if not exists( self.configFile ) or isfile( self.configFile ):
            print( 'Write new config file:', self.configFile )
            self.writeNewConfig( self.configFile )

    def getCurrentBank(self, bank):
        cfg = ConfigObj( self.configFile )
        for cfgBank, cfgBankValue in cfg.iteritems():
            if cfgBank == bank:
                for fieldNumber in cfgBankValue:
                    if not 'interestDate' in self.fields:
                        self.fields.append( [ 'interestDate', fieldNumber ] )
                    elif not 'date' in self.fields:
                        self.fields.append( [ 'date', fieldNumber ] )
                    elif not 'amount' in self.fields:
                        self.fields.append( [ 'amount', fieldNumber ] )
                    elif not 'memo' in self.fields:
                        self.fields.append( [ 'memo', fieldNumber ] )
                    elif not 'name' in self.fields:
                        self.fields.append( [ 'name', fieldNumber ] )
                    elif not 'account' in self.fields:
                        self.fields.append( [ 'account', fieldNumber ] )
                    elif not 'type' in self.fields:
                        self.fields.append( [ 'type', fieldNumber ] )
                    elif not 'debet/credit' in self.fields:
                        self.fields.append( [ 'debet/credit', fieldNumber ] )
                        
    def __init__(self):
        self.fields = []
            

if __name__ == '__main__':
    config = Config()
    config.checkForConfig()
    config.getCurrentBank( 'NL94INGB' )
