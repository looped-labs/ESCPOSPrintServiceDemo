# ESC POS Print Service Demo  
  
Printing to a ESC/POS Thermal Receipt printer is simple now. Just send an intent.  
  
## Print Intents to print PDF URL's
    Intent pi = new Intent();  
    pi.setAction("org.escpos.intent.action.PRINT");  
    pi.setPackage("com.loopedlabs.escposprintservice");  
    pi.putExtra("DATA_TYPE", "PDF_URL");  
    pi.putExtra(Intent.EXTRA_TEXT, "https://url.for.pdf");  
    startActivity(i);  
#### You can also print Images, HTML & PDF Urls
------------------------------------------------------------------------------------------
  ## HeadingPrint Intents to print PDF data in a Byte Array

    Intent pi = new Intent();  
    pi.setAction("org.escpos.intent.action.PRINT");  
    pi.setPackage("com.loopedlabs.escposprintservice");  
    pi.putExtra("PRINT_DATA", pdfByteArray);  
    pi.putExtra("DATA_TYPE", "PDF");  
    startActivity(i);
#### You can also print Images, HTML & PDF Byte Arrays
