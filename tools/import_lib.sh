# Use ENV File Binaries
source ../.env

HOME_DIR="${PWD}/..";
LIB_DIR="${HOME_DIR}/lib"
WIN64_DIR="${LIB_DIR}/win64"

check_pzhome () {
    if [ -n "${PZ_HOME:+set}" ]; then
        echo "ENV PZ_HOME is set."
    else
        echo "ENV PZ_HOME not set. Exiting.."
        exit 1
    fi
}

ensure_lib_dir () {
    if [ ! -d "$LIB_DIR" ]; then
        echo -n "The directory \"lib\" does not exist. Creating it now..."
        mkdir -p "$LIB_DIR"
        echo "DONE."
    fi
}

ensure_win64_dir () {
    if [ ! -d "$WIN64_DIR" ]; then
        echo -n "The directory \"lib/win64\" does not exist. Creating it now..."
        mkdir -p "$WIN64_DIR"
        echo "DONE."
    fi
}

copy_lib_dir () {
    echo -n "Copying root dll files.. "
    cd "$PZ_HOME"
    cp *.dll "$LIB_DIR"
    cd "$HOME_DIR"
    echo " DONE."
}

copy_win64_dir () {
    echo -n "Copying win64 dll files.. "
    cd "${PZ_HOME}/win64"
    cp *.dll "$WIN64_DIR"
    cd "$HOME_DIR"
    echo " DONE."
}

copy_lua_files () {
    echo -n "Copying Root Kahlua2 Files.. "
    cd "$PZ_HOME"
    cp "serialize.lua" "${HOME_DIR}/serialize.lua"
    cp "stdlib.lua" "${HOME_DIR}/stdlib.lua"
    cp "stdlib.lbc" "${HOME_DIR}/stdlib.lbc"
    cd "$HOME_DIR"
    echo " DONE."
}

check_pzhome
ensure_lib_dir
copy_lib_dir
ensure_win64_dir
copy_win64_dir
copy_lua_files

exit 0
