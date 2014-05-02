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
    # 5 description
    # 6 account
    # 7 type
    # 8 debit/credit

    def writeNewConfig(self,filename):
        # number is column to read
        # write space to use multiple columns for one field
        config = [
            'HASH = ba9998bcf7',
            'NL94INGB = 0,0,6,8 1,1,3,4,5 bij af',
            'NL91RABO = 7,2,4,10 11 12 13 14 15 16 17 18,6,4,-1,3 C D'
            ]            

        cfg = ConfigObj( config )
        cfg.filename = filename
        cfg.write()

    def addToConfig(self,fields):

        cfg = ConfigObj( self.configFile )

        if 'main account' in (x[0] for x in fields):
            key = fields['main account'][:8] 
        
            if 'interestDate' in (x[0] for x in fields):
                fields.append( fields['interestDate'] )
            else:
                fields.append( "" )
                
            if 'date' in (x[0] for x in fields):
                fields.append( [ 'date', fieldNumber ] )
            else:
                fields.append( "" )
                
            if 'amount' in (x[0] for x in fields):
                fields.append( [ 'amount', fieldNumber ] )
            else:
                fields.append( "" )
                
            if 'memo' in (x[0] for x in fields):
                fields.append( [ 'memo', fieldNumber ] )
            else:
                fields.append( "" )
                
            if 'description' in (x[0] for x in fields):
                fields.append( [ 'description', fieldNumber ] )
            else:
                fields.append( "" )
                
            if 'account' in (x[0] for x in fields):
                fields.append( [ 'account', fieldNumber ] )
            else:
                fields.append( "" )
                
            if 'type' in (x[0] for x in fields):
                fields.append( [ 'type', fieldNumber ] )
            else:
                fields.append( "" )
                
            if 'credit/debit' in (x[0] for x in fields):
                fields.append( [ 'credit/debit', fieldNumber ] )        
            else:
                fields.append( "" )
                

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
                    elif not 'description' in (x[0] for x in fields):
                        fields.append( [ 'description', fieldNumber ] )
                    elif not 'account' in (x[0] for x in fields):
                        fields.append( [ 'account', fieldNumber ] )
                    elif not 'type' in (x[0] for x in fields):
                        fields.append( [ 'type', fieldNumber ] )
                    elif not 'credit/debit' in (x[0] for x in fields):
                        fields.append( [ 'credit/debit', fieldNumber ] )

        return fields
                        
    def __init__(self):
        self.checkForConfig()
