ECFP feature lookup demo
========================

This is a usage example for [chemaxon.descriptors.ECFPFeatureLookup](https://www.chemaxon.com/jchem/doc/dev/java/api/index.html?chemaxon/descriptors/ECFPFeatureLookup.html) API. This project is intended for the active users of ChemAxon [ECFP](https://docs.chemaxon.com/pages/viewpage.action?pageId=41129785) implementation.

![Input structure (pyrrole)](/src/doc/pyrrole.png?raw=true "Input structure")
![Features (ECFP2)](/src/doc/pyrrole-ecfp2-features.png?raw=true "Features (ECFP2)")


Getting started
---------------

This project depends on [ChemAxon](https://www.chemaxon.com/) proprietary `jchem.jar` available in [JChem Suite](https://www.chemaxon.com/download/jchem-suite/) platform independent (.zip) distribution. This distribution can be downloaded manually or retrieved from <https://repository.chemaxon.com> with proper credentials. Both approach are detailed.


### Installing license

  - Make sure that ChemAxon licenses for the used functionalities are available and installed. For details see [ChemAxon Installing Licenses](http://www.chemaxon.com/marvin/help/licensedoc/install.html) documentation.

### Manual download of JChem distribution

  - Make sure that you have the required registration to access ChemAxon product downloads and download JChem Suite - platform independent (.zip) distribution from <https://www.chemaxon.com/download/jchem-suite/>. 
  - Unpack the downloaded file.
  - Locate file `jchem/lib/jchem.jar` in the unpacked directory.
  - Launch `./gradlew -Pcxn_jchem_jar=<PATH TO JCHEM JAR> installDist` 

Note that you can point to the location of `jchem.jar` in an existing JChem Suite installation.
  
### Using [ChemAxon Public Repository](https://repository.chemaxon.com)

  - Make sure that you have the username and password required to access ChemAxon Public Repository. For access send a request to <maven-repo-request@chemaxon.com>. For details see <https://docs.chemaxon.com/display/jchembase/Introduction+for+Java+applications#IntroductionforJavaapplications-swreq>
  - Launch `./gradlew -Pcxn_repo_user=<USERNAME> -Pcxn_repo_pass=<PASSWORD> installDist`

### Launching

  - To get command line help launch 
  
    build/install/ecfpf/bin/ecfpf.bat -h
    
  - To generate SMARTS illustration of ECFP features launch
  
    cat build/install/ecfpf/data/caffeine.smiles | build/install/ecfpf/bin/ecfpf -idname > features-caffeine-ecfp4.smarts

  - To use a custom ECFP config XML use command line option `-c <CONFIGFILE>`
  
    cat build/install/ecfpf/data/caffeine.smiles | build/install/ecfpf/bin/ecfpf -c build/install/ecfpf/data/ecfp-6.xml -idname > features-caffeine-ecfp6.smarts
  
### Other property setting mechanisms

The above examples used command line property settings (`-P<NAME>=<VALUE>`). There are other ways to pass properties to a gradle build script, for details see <http://mrhaki.blogspot.hu/2010/09/gradle-goodness-different-ways-to-set.html>. 


Known issues
------------
  
The two configurations (manual download of JChem distribution/using ChemAxon Public Repository) are not equivalent. In the first case only the specified `jchem.jar` file and the built `jar` file are used in the classpath of the launcher scripts. In the second case all of the dependencies of `jchem.jar` are included in the classpath of the launcher scripts. On windows the resulting long command line causes problems.

  
Licensing
---------

**This project** is distributed under the [Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0). Some
dependencies of this project are **ChemAxon proprietary products which are not covered by this license**. Please
note that redistribution of ChemAxon proprietary products is not allowed.
