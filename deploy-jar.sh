mvn deploy:deploy-file \
    -Dfile=target/guanxi-common-1.3.5.jar \
    -DgroupId=org.guanxi \
    -DartifactId=guanxi-common \
    -Dversion=1.3.5 \
    -Dpackaging=jar \
    -DgeneratePom=true \
    -DrepositoryId=source.uhi.ac.uk-repository \
    -Durl=dav:http://source.uhi.ac.uk/mvn
