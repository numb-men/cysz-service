package cn.hengyumo.humor.utils;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * ClassUtil
 *
 * @author 黄勇
 * @version 1.0
 * @since 2013/08/31 23:54
 */
public class ClassUtil {

    // 获取指定包名下的所有类
    public static List<Class<?>> getClassList(String packageName, boolean isRecursive) {
        List<Class<?>> classList = new ArrayList<>();
        try {
            Enumeration<URL> urls = Thread.currentThread().getContextClassLoader()
                    .getResources(packageName.replaceAll("\\.", "/"));
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                if (url != null) {
                    String protocol = url.getProtocol();
                    if (protocol.equals("file")) {
                        String packagePath = url.getPath();
                        addClass(classList, packagePath, packageName, isRecursive);
                    } else if (protocol.equals("jar")) {
                        JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
                        JarFile jarFile = jarURLConnection.getJarFile();
                        Enumeration<JarEntry> jarEntries = jarFile.entries();
                        while (jarEntries.hasMoreElements()) {
                            JarEntry jarEntry = jarEntries.nextElement();
                            String jarEntryName = jarEntry.getName();
                            if (jarEntryName.endsWith(".class")) {
                                String className = jarEntryName.substring(0,
                                        jarEntryName.lastIndexOf(".")).replaceAll("/", ".");
                                if (isRecursive ||
                                        className.substring(0, className.lastIndexOf(".")).equals(packageName)) {
                                    classList.add(Class.forName(className));
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return classList;
    }

    // 获取指定包名下的所有类（可根据注解进行过滤）
    public static List<Class<?>> getClassListByAnnotation(String packageName, Class<? extends Annotation> annotationClass) {
        List<Class<?>> classList = new ArrayList<>();
        try {
            Enumeration<URL> urls = Thread.currentThread().getContextClassLoader()
                    .getResources(packageName.replaceAll("\\.", "/"));
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                if (url != null) {
                    String protocol = url.getProtocol();
                    if (protocol.equals("file")) {
                        String packagePath = url.getPath();
                        addClassByAnnotation(classList, packagePath, packageName, annotationClass);
                    } else if (protocol.equals("jar")) {
                        JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
                        JarFile jarFile = jarURLConnection.getJarFile();
                        Enumeration<JarEntry> jarEntries = jarFile.entries();
                        while (jarEntries.hasMoreElements()) {
                            JarEntry jarEntry = jarEntries.nextElement();
                            String jarEntryName = jarEntry.getName();
                            if (jarEntryName.endsWith(".class")) {
                                String className = jarEntryName.substring(0, jarEntryName
                                        .lastIndexOf(".")).replaceAll("/", ".");
                                Class<?> cls = Class.forName(className);
                                if (cls.isAnnotationPresent(annotationClass)) {
                                    classList.add(cls);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return classList;
    }

    /**
     * 获取某包下所有类
     *
     * @param packageName 包名
     * @param isRecursion 是否遍历子包
     * @return 类的完整名称
     */
    public static Set<String> getClassName(String packageName, boolean isRecursion) {
        Set<String> classNames = null;
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        String packagePath = packageName.replace(".", "/");

        URL url = loader.getResource(packagePath);
        if (url != null) {
            String protocol = url.getProtocol();
            if (protocol.equals("file")) {
                classNames = getClassNameFromDir(url.getPath(), packageName, isRecursion);
            } else if (protocol.equals("jar")) {
                JarFile jarFile = null;
                try {
                    jarFile = ((JarURLConnection) url.openConnection()).getJarFile();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (jarFile != null) {
                    getClassNameFromJar(jarFile.entries(), packageName, isRecursion);
                }
            }
        } else {
            /*从所有的jar包中查找包名*/
            classNames = getClassNameFromJars(((URLClassLoader) loader)
                    .getURLs(), packageName, isRecursion);
        }

        return classNames;
    }

    /**
     * 从项目文件获取某包下所有类
     *
     * @param filePath    文件路径
     * @param packageName 包名
     * @param isRecursion 是否遍历子包
     * @return 类的完整名称
     */
    private static Set<String> getClassNameFromDir(String filePath, String packageName, boolean isRecursion) {
        Set<String> className = new HashSet<>();
        File file = new File(filePath);
        File[] files = file.listFiles();
        if (files != null) {
            for (File childFile : files) {
                if (childFile.isDirectory()) {
                    if (isRecursion) {
                        className.addAll(getClassNameFromDir(childFile.getPath(),
                                packageName + "." + childFile.getName(), isRecursion));
                    }
                } else {
                    String fileName = childFile.getName();
                    if (fileName.endsWith(".class") && !fileName.contains("$")) {
                        className.add(packageName + "." + fileName.replace(".class", ""));
                    }
                }
            }
        }

        return className;
    }


    /**
     * 从Jar包下获取所有类名
     *
     * @param jarEntries jarEntries
     * @param packageName 包名
     * @param isRecursion 是否遍历子包
     * @return 类名集合
     */
    private static Set<String> getClassNameFromJar(Enumeration<JarEntry> jarEntries,
                                                   String packageName, boolean isRecursion) {
        Set<String> classNames = new HashSet<>();

        while (jarEntries.hasMoreElements()) {
            JarEntry jarEntry = jarEntries.nextElement();
            if (!jarEntry.isDirectory()) {
                /*
                 * 这里是为了方便，先把"/" 转成 "." 再判断 ".class" 的做法可能会有bug
                 * (FIXME: 先把"/" 转成 "." 再判断 ".class" 的做法可能会有bug)
                 */
                String entryName = jarEntry.getName().replace("/", ".");
                if (entryName.endsWith(".class")
                        && !entryName.contains("$") && entryName.startsWith(packageName)) {
                    entryName = entryName.replace(".class", "");
                    if (isRecursion) {
                        classNames.add(entryName);
                    } else if (!entryName.replace(packageName + ".", "").contains(".")) {
                        classNames.add(entryName);
                    }
                }
            }
        }

        return classNames;
    }

    /**
     * 从所有jar中搜索该包，并获取该包下所有类
     *
     * @param urls        URL集合
     * @param packageName 包路径
     * @param isRecursion 是否遍历子包
     * @return 类的完整名称
     */
    private static Set<String> getClassNameFromJars(URL[] urls, String packageName, boolean isRecursion) {
        Set<String> classNames = new HashSet<String>();

        for (URL url : urls) {
            String classPath = url.getPath();

            //不必搜索classes文件夹
            if (classPath.endsWith("classes/")) {
                continue;
            }

            JarFile jarFile = null;
            try {
                jarFile = new JarFile(classPath.substring(classPath.indexOf("/")));
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (jarFile != null) {
                classNames.addAll(getClassNameFromJar(jarFile.entries(), packageName, isRecursion));
            }
        }

        return classNames;
    }

    private static void addClass(List<Class<?>> classList, String packagePath,
                                 String packageName, boolean isRecursive) {
        try {
            File[] files = getClassFiles(packagePath);
            if (files != null) {
                for (File file : files) {
                    String fileName = file.getName();
                    if (file.isFile()) {
                        String className = getClassName(packageName, fileName);
                        classList.add(Class.forName(className));
                    } else {
                        if (isRecursive) {
                            String subPackagePath = getSubPackagePath(packagePath, fileName);
                            String subPackageName = getSubPackageName(packageName, fileName);
                            addClass(classList, subPackagePath, subPackageName, isRecursive);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static File[] getClassFiles(String packagePath) {
        return new File(packagePath).listFiles(file ->
                file.isFile() && file.getName().endsWith(".class")
                        || file.isDirectory());
    }

    private static String getClassName(String packageName, String fileName) {
        String className = fileName.substring(0, fileName.lastIndexOf("."));
        if (packageName != null && !packageName.equals("")) {
            className = packageName + "." + className;
        }
        return className;
    }

    private static String getSubPackagePath(String packagePath, String filePath) {
        String subPackagePath = filePath;
        if (packagePath != null && !packagePath.equals("")) {
            subPackagePath = packagePath + "/" + subPackagePath;
        }
        return subPackagePath;
    }

    private static String getSubPackageName(String packageName, String filePath) {
        String subPackageName = filePath;
        if (packageName != null && !packageName.equals("")) {
            subPackageName = packageName + "." + subPackageName;
        }
        return subPackageName;
    }

    private static void addClassByAnnotation(List<Class<?>> classList, String packagePath, String packageName,
                                             Class<? extends Annotation> annotationClass) {
        try {
            File[] files = getClassFiles(packagePath);
            if (files != null) {
                for (File file : files) {
                    String fileName = file.getName();
                    if (file.isFile()) {
                        String className = getClassName(packageName, fileName);
                        Class<?> cls = Class.forName(className);
                        if (cls.isAnnotationPresent(annotationClass)) {
                            classList.add(cls);
                        }
                    } else {
                        String subPackagePath = getSubPackagePath(packagePath, fileName);
                        String subPackageName = getSubPackageName(packageName, fileName);
                        addClassByAnnotation(classList, subPackagePath, subPackageName, annotationClass);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
