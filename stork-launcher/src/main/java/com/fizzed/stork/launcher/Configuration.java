/*
 * Copyright 2014 Fizzed, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fizzed.stork.launcher;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Describes the launcher(s) to generate.
 * 
 * @author joelauer
 */
public class Configuration {
    
    static public enum Platform {
        LINUX,
        MAC_OSX,
        WINDOWS
    }
    
    static public enum Type {
        CONSOLE,
        DAEMON
    }
    
    static public enum WorkingDirMode {
        RETAIN,
        APP_HOME
    }
    
    static public enum DaemonMethod {
        NOHUP,
        JSLWIN,
        WINSW
    }
    
    @JsonIgnore
    private File file;
    
    // standard directories for app layout
    private String binDir = "bin";
    private String runDir = "run";
    private String shareDir = "share";
    private String logDir = "log";
    private String libDir = "lib";
    
    @NotNull @Size(min=1)
    private Set<Platform> platforms;
    
    @NotNull
    private String name;
    
    private String displayName;
    
    @NotNull
    private String domain;
    
    @NotNull
    private String shortDescription;
    private String longDescription;

    @NotNull
    private String mainClass;
    
    @NotNull
    private Type type;
    
    private WorkingDirMode workingDirMode;
    private String appArgs = "";
    private String javaArgs = "";
    private String extraAppArgs = "";
    private String extraJavaArgs = "";
    private String minJavaVersion = "1.6";
    private String maxJavaVersion = null;
    private Integer minJavaMemory = null;
    private Integer maxJavaMemory = null;
    private Integer minJavaMemoryPct = null;
    private Integer maxJavaMemoryPct = null;
    
    // java processes that are daemons should always add the -Xrs option
    // (especially if you are running via systemd) - otherwise systemd will
    // report the process exited with status code of 143...
    private boolean includeJavaXrs = true;
    
    // best effort to symlink java binary so process is named something more
    // friendly for users (only safe for daemons with unique names)
    // default name is "<app name>-java"
    private boolean symlinkJava = false;
    
    // should a java-detect helper script be included in share/helper
    private boolean includeJavaDetectHelper = false;
    
    // daemon pid will be tested after this amount of seconds to confirm it is
    // still running -- a simple way to verify that it likely started
    private Integer daemonMinLifetime = 5;
    
    private Map<Platform,PlatformConfiguration> platformConfigurations;
    
    private String systemdServiceSection;
    
    public Configuration() {
        this.platformConfigurations = new HashMap<>();
        // create default linux and windows configurations
        PlatformConfiguration linuxConfig = new PlatformConfiguration();
        // http://stackoverflow.com/questions/958249/whats-the-difference-between-nohup-and-a-daemon
        // really intersting discussion of NOHUP vs. other methods of daemonizing
        linuxConfig.setDaemonMethod(DaemonMethod.NOHUP);
        linuxConfig.setPrefixDir("/opt");
        this.platformConfigurations.put(Platform.LINUX, linuxConfig);
        PlatformConfiguration windowsConfig = new PlatformConfiguration();
        windowsConfig.setDaemonMethod(DaemonMethod.JSLWIN);
        this.platformConfigurations.put(Platform.WINDOWS, windowsConfig);
    }
    
    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getBinDir() {
        return binDir;
    }

    public void setBinDir(String binDir) {
        this.binDir = binDir;
    }

    public String getShareDir() {
        return shareDir;
    }

    public void setShareDir(String shareDir) {
        this.shareDir = shareDir;
    }

    public String getRunDir() {
        return runDir;
    }

    public void setRunDir(String runDir) {
        this.runDir = runDir;
    }

    public String getLogDir() {
        return logDir;
    }

    public void setLogDir(String logDir) {
        this.logDir = logDir;
    }

    public Set<Platform> getPlatforms() {
        return platforms;
    }

    public void setPlatforms(Set<Platform> platforms) {
        this.platforms = platforms;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
    
    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public String getMainClass() {
        return mainClass;
    }

    public void setMainClass(String mainClass) {
        this.mainClass = mainClass;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public WorkingDirMode getWorkingDirMode() {
        if (workingDirMode == null) {
            if (this.type == Type.CONSOLE) {
                return WorkingDirMode.RETAIN;
            } else if (this.type == Type.DAEMON) {
                return WorkingDirMode.APP_HOME;
            }
        }
        return workingDirMode;
    }

    public void setWorkingDirMode(WorkingDirMode workingDirMode) {
        this.workingDirMode = workingDirMode;
    }

    public String getAppArgs() {
        return appArgs;
    }

    public void setAppArgs(String appArgs) {
        this.appArgs = appArgs;
    }

    public String getExtraAppArgs() {
        return extraAppArgs;
    }

    public void setExtraAppArgs(String extraAppArgs) {
        this.extraAppArgs = extraAppArgs;
    }

    public String getJavaArgs() {
        return javaArgs;
    }

    public void setJavaArgs(String javaArgs) {
        this.javaArgs = javaArgs;
    }

    public String getExtraJavaArgs() {
        return extraJavaArgs;
    }

    public void setExtraJavaArgs(String extraJavaArgs) {
        this.extraJavaArgs = extraJavaArgs;
    }
    
    public String getLibDir() {
        return libDir;
    }

    public void setLibDir(String libDir) {
        this.libDir = libDir;
    }

    public boolean isIncludeJavaXrs() {
        return includeJavaXrs;
    }

    public void setIncludeJavaXrs(boolean includeJavaXrs) {
        this.includeJavaXrs = includeJavaXrs;
    }

    public String getMinJavaVersion() {
        return minJavaVersion;
    }

    public void setMinJavaVersion(String minJavaVersion) {
        this.minJavaVersion = minJavaVersion;
    }

    public String getMaxJavaVersion() {
        return maxJavaVersion;
    }

    public void setMaxJavaVersion(String maxJavaVersion) {
        this.maxJavaVersion = maxJavaVersion;
    }

    public Integer getMinJavaMemory() {
        return minJavaMemory;
    }

    public void setMinJavaMemory(Integer minJavaMemory) {
        this.minJavaMemory = minJavaMemory;
    }

    public Integer getMaxJavaMemory() {
        return maxJavaMemory;
    }

    public void setMaxJavaMemory(Integer maxJavaMemory) {
        this.maxJavaMemory = maxJavaMemory;
    }

    public Integer getMinJavaMemoryPct() {
        return minJavaMemoryPct;
    }

    public void setMinJavaMemoryPct(Integer minJavaMemoryPct) {
        this.minJavaMemoryPct = minJavaMemoryPct;
    }

    public Integer getMaxJavaMemoryPct() {
        return maxJavaMemoryPct;
    }

    public void setMaxJavaMemoryPct(Integer maxJavaMemoryPct) {
        this.maxJavaMemoryPct = maxJavaMemoryPct;
    }

    public boolean isSymlinkJava() {
        return symlinkJava;
    }

    public void setSymlinkJava(boolean symlinkJava) {
        this.symlinkJava = symlinkJava;
    }

    public boolean isIncludeJavaDetectHelper() {
        return includeJavaDetectHelper;
    }

    public void setIncludeJavaDetectHelper(boolean includeJavaDetectHelper) {
        this.includeJavaDetectHelper = includeJavaDetectHelper;
    }

    public Integer getDaemonMinLifetime() {
        return daemonMinLifetime;
    }

    public void setDaemonMinLifetime(Integer daemonMinLifetime) {
        this.daemonMinLifetime = daemonMinLifetime;
    }

    public Map<Platform, PlatformConfiguration> getPlatformConfigurations() {
        return platformConfigurations;
    }

    public void setPlatformConfigurations(Map<Platform, PlatformConfiguration> platformConfigurations) {
        this.platformConfigurations = platformConfigurations;
    }
    
    
    public PlatformConfiguration getPlatformConfiguration(Platform platform) {
        if (platform == null) {
            return null;
        }
        return this.platformConfigurations.get(platform);
    }
   
    
    public DaemonMethod getPlatformDaemonMethod(String platformName) {
        Platform platform = Platform.valueOf(platformName);
        return getPlatformDaemonMethod(platform);
    }
    
    public DaemonMethod getPlatformDaemonMethod(Platform platform) {
        PlatformConfiguration pc = getPlatformConfiguration(platform);
        if (pc == null) {
            return null;
        }
        
        DaemonMethod v = pc.getDaemonMethod();
        
        // fallback to linux for mac
        if (v == null && platform == Platform.MAC_OSX) {
            return getPlatformDaemonMethod(Platform.LINUX);
        }
        
        return v;
    }
    
    
    public String getPlatformUser(String platformName) {
        Platform platform = Platform.valueOf(platformName);
        return getPlatformUser(platform);
    }
    
    public String getPlatformUser(Platform platform) {
        PlatformConfiguration pc = getPlatformConfiguration(platform);
        if (pc == null) {
            return null;
        }
        
        String v = pc.getUser();
        
        // fallback to linux for mac
        if (v == null && platform == Platform.MAC_OSX) {
            return getPlatformUser(Platform.LINUX);
        }
        
        return v;
    }
    
    
    public String getPlatformGroup(String platformName) {
        Platform platform = Platform.valueOf(platformName);
        return getPlatformUser(platform);
    }
    
    public String getPlatformGroup(Platform platform) {
        PlatformConfiguration pc = getPlatformConfiguration(platform);
        if (pc == null) {
            return null;
        }
        
        String v = pc.getGroup();
        
        // fallback to linux for mac
        if (v == null && platform == Platform.MAC_OSX) {
            return getPlatformGroup(Platform.LINUX);
        }
        
        return v;
    }
    
    
    public String getPlatformPrefixDir(String platformName) {
        Platform platform = Platform.valueOf(platformName);
        return getPlatformPrefixDir(platform);
    }
    
    public String getPlatformPrefixDir(Platform platform) {
        PlatformConfiguration pc = getPlatformConfiguration(platform);
        if (pc == null) {
            return null;
        }
        
        String v = pc.getPrefixDir();
        
        // fallback to linux for mac
        if (v == null && platform == Platform.MAC_OSX) {
            return getPlatformPrefixDir(Platform.LINUX);
        }
        
        return v;
    }
    
    
    public String getPlatformLogDir(String platformName) {
        Platform platform = Platform.valueOf(platformName);
        return getPlatformLogDir(platform);
    }
    
    public String getPlatformLogDir(Platform platform) {
        PlatformConfiguration pc = getPlatformConfiguration(platform);
        if (pc == null) {
            return null;
        }
        
        String v = pc.getLogDir();
        
        return v;
    }
    
    
    public String getPlatformRunDir(String platformName) {
        Platform platform = Platform.valueOf(platformName);
        return getPlatformRunDir(platform);
    }
    
    public String getPlatformRunDir(Platform platform) {
        PlatformConfiguration pc = getPlatformConfiguration(platform);
        if (pc == null) {
            return null;
        }
        
        String v = pc.getRunDir();
        
        return v;
    }

    public String getSystemdServiceSection() {
        return systemdServiceSection;
    }

    public void setSystemdServiceSection(String systemdServiceSection) {
        this.systemdServiceSection = systemdServiceSection;
    }

}