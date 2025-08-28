#!/bin/sh

export GRADLE_USER_HOME="${GRADLE_USER_HOME:-${HOME}/.gradle}"

executable="$(dirname "$0")/gradle/wrapper/gradle-wrapper.jar"

if [ ! -f "$executable" ]; then
    echo "Error: $executable does not exist!" >&2
    exit 1
fi

export JAVA_HOME="${JAVA_HOME:-${JDK_HOME}}"

if [ -n "$JAVA_HOME" ]; then
    java_cmd="$JAVA_HOME/bin/java"
else
    java_cmd="java"
    echo "Warning: JAVA_HOME environment variable is not set." >&2
fi

exec "$java_cmd" "-Dorg.gradle.appname=$0" -classpath "$executable" org.gradle.wrapper.GradleWrapperMain "$@"