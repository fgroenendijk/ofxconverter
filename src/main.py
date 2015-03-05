import sys
import tkFileDialog
from config import Config
from input.bank import Bank
from output.ofx import Ofx
from model.bankStatement import BankStatement
from model.transaction import Transaction
from csvReader import CsvReader

from PIL import ImageTk

import Tkconstants as constants
import ttk
from Tkinter import Label, Checkbutton, IntVar, StringVar, Scrollbar, Canvas, Message, Frame, Menu
from Tkinter import PhotoImage, Entry, Button, Tk, Text
import FixTk # needed for cx_freeze
import csv
import collections
from io import open

class OfxConverter(Frame):
  
    def __init__(self, parent):
        Frame.__init__(self, parent, background=u"white")   
         
        self.parent = parent
        self.guiMap = []
        self.debitValue = StringVar()
        self.creditValue = StringVar()
        self.row = 0
        self.frame = Frame()

        self.UNUSED = u"Unused"
        
        self.initUI()

    def writeLog(self,*msgs):
        self.log[u'state'] = u'normal'
        if self.log.index(u'end-1c')!=u'1.0':
            self.log.insert(u'end', u'\n')
        for msg in msgs:
            self.log.insert(u'end', msg)
##        self.log['state'] = 'disabled'

    def help(self):
        t = Toplevel()
        t.wm_title(u"About")
        t.transient()

        photo = ImageTk.PhotoImage(file=u"chameleon.png")
        
##        photo = PhotoImage(file="chameleon.gif")
        w = Label(t, image=photo)
        w.photo = photo
        w.pack(fill=constants.BOTH,side=constants.LEFT,expand=True)
        
        l = Label(t, text=u"OFX Converter\n\n"+
                  u"Convert files in csv format to the\nofx format " +
                  u"which is used by a lot\nof accounting programs " +
                  u"like gnucash\n\n Written in Python 3\nby Floris Groenendijk"
                  )
        l.pack(side=constants.TOP, fill=u"both", expand=False, padx=20, pady=20)

    def onComboboxChanged(self, event):
        if event.widget.get() == u'credit/debit':
            # check which checkbox in which column was changed
            for i in xrange(len(self.comboBoxes)-1):
                if self.comboBoxes[i] == event.widget:
                    break
                
            values = []

            # retrieve the values of the labels in that column
            for j in xrange(len(self.labels)):
                if self.labels[j][i][u'text'] not in values:
                    values.append(self.labels[j][i][u'text'])
            
            self.creditCombo[u'values'] = values
            self.creditCombo.current( 0 )

            if len( values ) > 1:
                self.debitCombo[u'values'] = values
                self.debitCombo.current( 1 )

    def onFrameConfigure(self, event):
        # Reset the scroll region to encompass the inner frame
        self.canvas.configure(scrollregion=self.canvas.bbox(u"all"))

    def initUI(self):
      
        self.parent.title(u"OFX Converter")

        menubar = Menu(self.parent)
        self.parent.config(menu=menubar)
        
        fileMenu = Menu(menubar)
        fileMenu.add_command(label=u"Open", command=self.openFile, accelerator=u"Ctrl-o")
        fileMenu.add_command(label=u"Exit", command=self.onExit, accelerator=u"Ctrl-e", underline=1 )
        menubar.add_cascade(label=u"File", menu=fileMenu)
        helpMenu = Menu(menubar)
        helpMenu.add_command(label=u"About", command=self.help, accelerator=u"Ctrl-i")
        menubar.add_cascade(label=u"Info", menu=helpMenu)

        notebook = ttk.Notebook( self.parent )
        tabFilesMain = Frame( notebook )

        self.tabFiles = Frame( tabFilesMain )
        self.tabFiles.pack(fill=constants.BOTH)

        button = ttk.Button( tabFilesMain, command=self.parseFile, text=u"Process" )
        button.pack(side=u"bottom", fill=constants.X, padx=5, pady=5)
        
        tabLogging = Canvas( notebook )
        tabLogging.grid_rowconfigure(0, weight=1)
        tabLogging.grid_columnconfigure(0, weight=1)

        # begin of custom csv frame

        tabCustomCsv = Frame( notebook )

        verticalScrollFrame = Frame( tabCustomCsv )
        verticalScrollFrame.grid( row=0, column=0, rowspan=3, sticky=u"WNS" )

        tabCustomCsv.grid_columnconfigure(0, weight=0)
        tabCustomCsv.grid_columnconfigure(1, weight=1)
        tabCustomCsv.grid_rowconfigure(2,weight=1)

        self.comboFrame = Frame( tabCustomCsv )
        self.comboFrame.grid( row=1, column=1, sticky=u"WE" )
 
        canvasFrame = Frame( tabCustomCsv)
        canvasFrame.grid( row=2, column=1, sticky=u"NEWS" )

        horizontalScrollFrame = Frame( tabCustomCsv )
        horizontalScrollFrame.grid( row=3, column=1, columnspan=1, sticky=u"WE" )
        
        self.canvas = Canvas( canvasFrame, highlightthickness=0 )

        scrollbar=Scrollbar( horizontalScrollFrame,orient=u"horizontal",command=self.canvas.xview)
        self.canvas.configure(xscrollcommand=scrollbar.set)

        self.canvas.pack(fill=constants.BOTH,expand=True)
        scrollbar.pack(side=u"bottom", fill=constants.X)

        scrollbar=Scrollbar( verticalScrollFrame,orient=u"vertical",command=self.canvas.yview)
        self.canvas.configure(yscrollcommand=scrollbar.set)

        scrollbar.pack(side=u"left", fill=constants.Y)
        self.canvas.pack(fill=constants.BOTH,expand=1,anchor=u"nw")

        # end of custom csv frame

        # begin of config frame

        configurationFrame = Frame( tabCustomCsv )

        Label( configurationFrame, text=u"Values to determine whether the debit field concerns a debit or credit transaction and set the currency" ).pack(anchor=constants.W)

        currencyLine = Frame( configurationFrame )
        currencyLine.pack(fill=constants.X)
        Label( currencyLine, text=u"currency", width=7, anchor=constants.W ).pack(side=constants.LEFT)
        self.currencyCombo = ttk.Combobox( currencyLine,width=30,text=u"currency" )
        self.currencyCombo.pack(side=constants.LEFT)

        config = Config()
        self.currencies = config.getCurrencies()
        self.currencyCombo[u'values'] = list(sorted(self.currencies.keys()))

        Label( configurationFrame, text=u"credit", width=7, anchor=constants.W ).pack(side=constants.LEFT)
        self.creditCombo = ttk.Combobox(configurationFrame,width=10,text=u"credit")
        self.creditCombo.pack(side=constants.LEFT)

        Label( configurationFrame, text=u"debit", width=6, anchor=constants.W ).pack(side=constants.LEFT)
        self.debitCombo = ttk.Combobox( configurationFrame,width=10,text=u"debit" )
        self.debitCombo.pack(side=constants.LEFT)

        Button( configurationFrame, text=u"save configuration", command=self.saveConfig ).pack(side=constants.RIGHT )

        configurationFrame.grid( row=4, column=0, columnspan=2, sticky=u"WES")

        # end of config frame

        self.log = Text(tabLogging, wrap=u'word')
        self.log.grid(row=0,column=0,sticky=u'news')

        hScroll = Scrollbar(tabLogging, orient=constants.HORIZONTAL, command=self.log.xview)
        hScroll.grid(row=1, column=0, sticky=u'we')
        vScroll = Scrollbar(tabLogging, orient=constants.VERTICAL, command=self.log.yview)
        vScroll.grid(row=0, column=1, sticky=u'ns')
        self.log.configure(xscrollcommand=hScroll.set, yscrollcommand=vScroll.set)

        notebook.add( tabFilesMain, text=u"Files to process" )
        notebook.add( tabCustomCsv, text=u"Custom csv" )
        notebook.add( tabLogging, text=u"Logging" )

        notebook.pack(fill=constants.BOTH,expand=1,anchor=constants.N)

        self.tabFiles.grid_columnconfigure( 0, weight=1 ) 

    def addFile(self,filename,ibans):
        if filename != u"" and len(ibans) > 0:
            Label(self.tabFiles, text=filename,
                    borderwidth=3).grid(row=self.row,column=0,sticky=constants.W,padx=1)
            
            ibanList = []
            for iban in ibans:
                ibanList.append( iban[:8] )
            combo = ttk.Combobox(self.tabFiles,values=ibanList)
            combo.current(0)
            if len(ibanList) == 1:
                combo.configure(state=constants.DISABLED)
            combo.grid(row=self.row,column=1,sticky=constants.E,padx=1)
            
            state = IntVar()
            c = Checkbutton(self.tabFiles,variable=state)
            c.grid(row=self.row,column=2)
            self.row += 1
            ttk.Separator(self.tabFiles).grid(row=self.row, sticky=u"ew", columnspan=3 )
            self.row += 1
            self.guiMap.append( [ filename, ibans, combo, c, state ] )

    def addFileToCustomTab(self,filename):
        if filename != u"":

            if self.frame:
                self.frame.pack_forget()
                self.frame.destroy()
            self.frame = Frame( self.canvas )
            self.canvas.create_window((0,0),window=self.frame,anchor=u'nw')
            self.frame.bind(u"<Configure>", self.onFrameConfigure)
             
            file = csv.reader( open(filename) )
            lines = 1

            transaction = Transaction()
            fields = transaction.fields
            fields.insert(0,u"main account")
            fields.insert(0,self.UNUSED)

            self.comboBoxes = []
            self.labels = collections.defaultdict(list)
            
            for row in file:
                column = 0
                for field in row:
                    if lines == 1:
                        combo = ttk.Combobox(self.frame,values=transaction.fields,state=u"readonly")
                        combo.current(0)
                        combo.grid(row=0,column=column,sticky=constants.W)
                        self.comboBoxes.append( combo )
                        combo.bind(u'<<ComboboxSelected>>', self.onComboboxChanged)
                        nextColumn = column + 1
                        ttk.Separator(self.frame,orient=constants.VERTICAL).grid(row=0, column=nextColumn, sticky=u"ns")
                            
                    label = Label(self.frame,text=field,borderwidth=3)
                    label.grid(row=lines,column=column,sticky=constants.W,padx=1)
                    self.labels[lines-1].append( label )
                    column = column + 1
                    ttk.Separator(self.frame,orient=constants.VERTICAL).grid(row=lines, column=column, sticky=u"ns")
                    column = column + 1

                lines = lines + 1
                if lines > 11:
                    break

    def saveConfig(self):
        fields = {}
        memos = []
        for i in xrange( len(self.comboBoxes) - 1 ):
            key = self.comboBoxes[i].get()
            if key == self.UNUSED:
                continue
            elif key == u'credit/debit':
                fields[ key ] = u' '.join( [ unicode(i), self.creditCombo.get(), self.debitCombo.get() ] )
            elif key == u'memo':
                memos.append( unicode(i) )
            elif key == u'main account':
                fields[ key ] = self.labels[0][i][u'text']
            else:
                fields[ key ] = i

        if len(memos) > 0:
            fields[u'memo'] = u' '.join( memos )

        config = Config()
        (bankKey, bankValue) = config.addToConfig( fields )
        self.writeLog( u'key', bankKey, u"with value", bankValue, u"added to config file" ) 

    def openFile(self):
        if sys.version_info >= (3,0):
            filename = filedialog.askopenfilename(parent=self.parent,
                                                filetypes=[(u'Csv files',u'.csv'),
                                                           (u'All Files',u'.*')],
                                                title=u'Select the csv')
        else:
            filename = tkFileDialog.askopenfilename(parent=self.parent,
                                                filetypes=[(u'Csv files',u'.csv'),
                                                           (u'All Files',u'.*')],
                                                title=u'Select the csv')
        if filename != u"":
            self.writeLog( u'File added: ', filename )

            bank = Bank()

            ibans = bank.searchMainIban( filename )

            # If mainIban contains more than one iban,
            # let the user select which one is the main iban
            # Otherwise we know the bank this csv belongs to

            if len( ibans ) > 1:
                self.writeLog( u'there\'s too many ibans, please select one from the list' )
            elif len( ibans ) == 0:
                self.writeLog( u'No ibans found, is the file correct?' )
                ## adding file to custom csv tab
                
            else:
                self.writeLog( u'Found iban: ', ibans[0][:8] )
                ibanType = ibans[0][:8]

            self.addFile(filename,ibans)
            self.addFileToCustomTab( filename )


    def parseFile(self):

        for guiLine in self.guiMap:
            ( filename, ibans, combo, checkButton, state ) = guiLine

            if state.get():
                ibanType = combo.get()
                
                config = Config()
                fields = config.getCurrentBank( ibanType )

                bankStatement = BankStatement()

                bankStatement.account = ibans[0]

                csvReader = CsvReader(fields)
            
                csvReader.readFile( filename, bankStatement )

                ofx = Ofx()

                ofx.createXmlFile( filename, bankStatement )
                checkButton.configure(state=constants.DISABLED)

    def onExit(self):
        quit()

if __name__ == u'__main__':

    root = Tk()
    root.option_add(u'*tearOff', False)

    root.geometry(u"700x400+300+300")

    app = OfxConverter(root)
    root.mainloop()
        
