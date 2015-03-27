#ESC POS Print Service Demo

Printing to a ESC/POS printer is simple now. Just send an intent

    List<Byte> myBytes = new ArrayList<Byte>();
    myBytes.clear();

    String txt = "Hello Printer !";

    byte[] data = txt.getBytes();
       for (byte b : data) {
       myBytes.add(b);
    }
    IntentPrint(myBytes);
    //will send the intent to print the Hello Printer !.

    public boolean IntentPrint(byte[] myBytes) {
            if (isIntentAvailable(mContext, "org.escpos.intent.action.PRINT")) {
                Intent i = new Intent();

                i.setAction("org.escpos.intent.action.PRINT");
                byte[] result = new byte[myBytes.size()];
                for (int x = 0; x < myBytes.size(); x++) {
                    result[x] = myBytes.get(x).byteValue();
                }
                i.putExtra("PRINT_DATA", result);
                startActivity(i);
                return true;
            } else {
                String appPackageName = "com.loopedlabs.escposprintservice";
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
                return false;
            }
        }

