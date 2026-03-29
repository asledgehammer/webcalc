# Use ENV File Binaries
source ../.env

# Build local Artifact for Project Zomboid
mvn install:install-file -Dfile="${PZ_HOME}/${MVN_TARGET}" -DgroupId="${MVN_GROUP}" -DartifactId="${MVN_ARTIFACT}" -Dversion="${PZ_VERSION}" -Dpackaging=jar
# mvn
