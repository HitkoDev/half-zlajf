package mygame;

public class Main  {

    public static void main(String[] args) {
        String n = "Natives";
        String osName = System.getProperty("os.name");
        if(osName.startsWith("Windows"))
            n += "/windows";
        else if(osName.startsWith("Linux") || osName.startsWith("FreeBSD") || osName.startsWith("SunOS") || osName.startsWith("Unix"))
            n += "/linux";
        else if(osName.startsWith("Mac OS X") || osName.startsWith("Darwin"))
            n += "/macosx";
        else
            throw new LinkageError("Unknown platform: " + osName);
        
        n += "/" + (System.getProperty("os.arch").contains("64") ? "x64" : "x86");
        
        try {
            java.util.jar.JarFile jarfile = new java.util.jar.JarFile(new java.io.File("lib/assets.jar")); //jar file path(here sqljdbc4.jar)
            java.util.Enumeration<java.util.jar.JarEntry> enu= jarfile.entries();
            while(enu.hasMoreElements()) {
                String destdir = "natives";   
                java.util.jar.JarEntry je = enu.nextElement();

                if(!je.getName().startsWith(n)) continue;
                String name = je.getName().substring(8);
                if(name.isEmpty()) continue;

                java.io.File fl = new java.io.File(destdir, name);
                if(!fl.exists()) {
                    fl.getParentFile().mkdirs();
                    fl = new java.io.File(destdir, name);
                }
                if(je.isDirectory()) continue;
                java.io.InputStream is = jarfile.getInputStream(je);
                java.io.FileOutputStream fo = new java.io.FileOutputStream(fl);
                while(is.available()>0) fo.write(is.read());
                fo.close();
                is.close();
            }

            System.setProperty("java.library.path", "natives/");
        } catch (Exception ex) {
            System.setProperty("java.library.path", "assets/Natives/");
        }
        
        new HelloWorld().run();
    }
}
