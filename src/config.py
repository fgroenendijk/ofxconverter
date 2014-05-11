from configobj import ConfigObj
from os.path import expanduser
from os.path import join
from os.path import isdir
from os.path import isfile
from os.path import exists
from os import makedirs
import errno
import pygit2

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
        cfgValue = []

        if 'main account' in fields:
            key = fields['main account'][:8] 
        
            if 'interestDate' in fields:
                cfgValue.append( str(fields['interestDate']) )
            else:
                cfgValue.append( '-1' )
                
            if 'date' in fields:
                cfgValue.append( str(fields['date']) )
            else:
                cfgValue.append( '-1' )
                
            if 'amount' in fields:
                cfgValue.append( str(fields['amount']) )
            else:
                cfgValue.append( '-1' )
                
            if 'memo' in fields:
                cfgValue.append( str(fields['memo']) )
            else:
                cfgValue.append( '-1' )
                
            if 'description' in fields:
                cfgValue.append( str(fields['description']) )
            else:
                cfgValue.append( '-1' )
                
            if 'account' in fields:
                cfgValue.append( str(fields['account']) )
            else:
                cfgValue.append( '-1' )
                
            if 'type' in fields:
                cfgValue.append( str(fields['type']) )
            else:
                cfgValue.append( '-1' )
                
            if 'credit/debit' in fields:
                cfgValue.append( str(fields['credit/debit']) )        
            else:
                cfgValue.append( '-1' )

        cfg[ key ] = cfgValue
        cfg.write()

        return [ key, cfgValue ]
                

    def checkForConfig(self):
        home = expanduser("~")

        configDir = join( home, '.ofxconverter' )

        makedirs( configDir, exist_ok=True )

        repo_url = 'git://git@git.code.sf.net/p/ofxconverter/code'

        self.configFile = join( configDir, 'banks.config' )
        
        if not exists( self.configFile ) or not isfile( self.configFile ):
            try:
                repo = pygit2.clone_repository(repo_url, configDir, checkout_branch='config')
            except KeyError:
                print( "Repository at", configDir, "already initialized" )
        elif isfile( self.configFile ):
            repo = pygit2.Repository( configDir )
            repo.checkout( 'config' )
                

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
