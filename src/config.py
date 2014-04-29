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
        # number is column to read
        # write space to use multiple columns for one field
        config = [
            'HASH = ba9998bcf7',
            'NL94INGB = 0,0,6,8 1,1,3,4,5',
            'NL91RABO = 0,0,0,0,0,0,0,0'
            ]            

        cfg = ConfigObj( config )
        cfg.filename = filename
        cfg.write()

    def checkForConfig(self):
        home = expanduser("~")
        configDir = join( home, '.ofxconverter' )

        makedirs( configDir, exist_ok=True )       
        self.configFile = join( configDir, 'banks.config' )
        
        if not exists( self.configFile ) or not isfile( self.configFile ):
            print( 'Write new config file:', self.configFile )
            self.writeNewConfig( self.configFile )
        elif isfile( self.configFile ):
            cfg = ConfigObj( self.configFile )
            for cfgKey, cfgValue in cfg.iteritems():
                print( cfgKey, cfgValue )
                if cfgKey == 'HASH':
                    if cfgValue != '':
                        self.writeNewConfig( self.configFile )
                

    def getCurrentBank(self, bank):
        cfg = ConfigObj( self.configFile )
        fields = []
        for cfgBank, cfgBankValue in cfg.iteritems():
            if cfgBank == bank:
                for fieldNumber in cfgBankValue:
                    print( fieldNumber )
                    if not 'interestDate' in (x[0] for x in fields):
                        fields.append( [ 'interestDate', fieldNumber ] )
                    elif not 'date' in (x[0] for x in fields):
                        fields.append( [ 'date', fieldNumber ] )
                    elif not 'amount' in (x[0] for x in fields):
                        fields.append( [ 'amount', fieldNumber ] )
                    elif not 'memo' in (x[0] for x in fields):
                        fields.append( [ 'memo', fieldNumber ] )
                    elif not 'name' in (x[0] for x in fields):
                        fields.append( [ 'name', fieldNumber ] )
                    elif not 'account' in (x[0] for x in fields):
                        fields.append( [ 'account', fieldNumber ] )
                    elif not 'type' in (x[0] for x in fields):
                        fields.append( [ 'type', fieldNumber ] )
                    elif not 'debet/credit' in (x[0] for x in fields):
                        fields.append( [ 'debet/credit', fieldNumber ] )

        return fields
                        
    def __init__(self):
        self.checkForConfig()
