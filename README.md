# thinApkFile
## Description

thinApkFile is a gradle plugin to remove useless file in apk to reduce apk size, in the current case,
this plugin just delete the java resource in you apk. For example, if you use kotlin、rxJava、okHttp or other
  library contains java resource in your project, your apk will like this image

![raw apk image](https://github.com/skyinu/thinApkFile/blob/master/art/origin_size.png)

then if you apply this plugin to your project,your apk will like this

![thin apk image](https://github.com/skyinu/thinApkFile/blob/master/art/thin_size.png)

## Usage

add classpath to your project

```
classpath 'com.skyinu:thinApkFilePlugin:0.1.4'
```

then apply the plugin will be ok

```
apply plugin: 'thinApkFile'
```

## Confirguration

```
thinApk{
    thinSwitch true //plugin switch
    printDetail true // log switch, true represenet to print more log to file
    keepRules [".*publicsuffixes.gz$"] // keep rules to indicate files which you don't want to remove
}
```

## Change

+ bug fix, keep file  `publicsuffixes.gz`- okhttp depends on the file publicsuffixes.gz, see more detail at https://publicsuffix.org
