package game;

public class Main  {

    public static void main(String[] args) {
        setNatives();
        
        new HelloWorld().run();
    }
    
    // Funkcija, ki pripravi native knjižnjice za interakcijo z OpenGLjem
    private static void setNatives(){
        // Ugotovi, katere native datoteke potrebuje
        String natives = "";
        String osName = System.getProperty("os.name");
        if(osName.startsWith("Windows"))
            natives += "windows";
        else if(osName.startsWith("Linux") || osName.startsWith("FreeBSD") || osName.startsWith("SunOS") || osName.startsWith("Unix"))
            natives += "linux";
        else if(osName.startsWith("Mac OS X") || osName.startsWith("Darwin"))
            natives += "macosx";
        else
            throw new LinkageError("Unknown platform: " + osName);
        natives += "/" + (System.getProperty("os.arch").contains("64") ? "x64" : "x86");
        try {
            java.util.jar.JarFile jarfile = new java.util.jar.JarFile(new java.io.File("lib/assets.jar")); //jar file path(here sqljdbc4.jar)
            java.util.Enumeration<java.util.jar.JarEntry> enu= jarfile.entries();
            String destdir = "natives";     // Target folder
            while(enu.hasMoreElements()) {
                java.util.jar.JarEntry je = enu.nextElement();
                String name = je.getName().length() > 8 ? je.getName().substring(8) : "";
                if(je.isDirectory() || !name.startsWith(natives)) continue;     // Preskoči nepotrebe datoteke
                java.io.File fl = new java.io.File(destdir, name);
                if(!fl.exists()) {
                    fl.getParentFile().mkdirs();
                    java.io.InputStream is = jarfile.getInputStream(je);
                    java.io.FileOutputStream fo = new java.io.FileOutputStream(fl);
                    while(is.available()>0) fo.write(is.read());
                    fo.close();
                    is.close();
                }
            }
            System.setProperty("java.library.path", "natives/");            // Uporabi datoteke iz jara
        } catch (Exception ex) {
            System.setProperty("java.library.path", "assets/Natives/");     // Fallback za poganjanje iz netbeansa
        }
    }
}
