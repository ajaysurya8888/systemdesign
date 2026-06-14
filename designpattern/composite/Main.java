package composite;

public class Main {

    public static void main(String[] args) {

        // --- Leaf files ---
        File resume   = new File("resume.pdf",    120);
        File photo    = new File("photo.jpg",     540);
        File notes    = new File("notes.txt",      18);

        // --- src/ directory ---
        Directory src = new Directory("src");
        src.add(new File("Main.java",    5));
        src.add(new File("Utils.java",   8));
        src.add(new File("Config.java",  3));

        // --- projects/ directory contains src/ and its own files ---
        Directory projects = new Directory("projects");
        projects.add(new File("app.java",       22));
        projects.add(new File("build.gradle",    4));
        projects.add(src);

        // --- downloads/ directory ---
        Directory downloads = new Directory("downloads");
        downloads.add(new File("setup.exe",   4096));
        downloads.add(new File("movie.mp4",  72000));

        // --- root/ directory — top of tree ---
        Directory root = new Directory("root");
        root.add(resume);
        root.add(photo);
        root.add(notes);
        root.add(projects);
        root.add(downloads);

        // display full tree
        System.out.println("===== File System Tree =====");
        root.display("");

        // getSizeKB() works uniformly on both File and Directory
        System.out.println("\n===== Size Report =====");
        System.out.println("resume.pdf   : " + resume.getSizeKB()    + " KB");
        System.out.println("src/         : " + src.getSizeKB()       + " KB");
        System.out.println("projects/    : " + projects.getSizeKB()  + " KB");
        System.out.println("downloads/   : " + downloads.getSizeKB() + " KB");
        System.out.println("root/ (total): " + root.getSizeKB()      + " KB");
    }
}