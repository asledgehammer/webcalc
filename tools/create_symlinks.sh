# Use ENV File Binaries
source ../.env

HOME_DIR="${PWD}/..";
MEDIA_DIR="${HOME_DIR}/media";
MODS_DIR="${HOME_DIR}/mods";

check_pzhome () {
    if [ -n "${PZ_HOME:+set}" ]; then
        echo "ENV PZ_HOME is set."
    else
        echo "ENV PZ_HOME not set. Exiting.."
        exit 1
    fi
}

create_media_symlink () {
    if [ -L "$MEDIA_DIR" ]; then
        echo -n "EXISTS: \"/media\" "
    else
        echo -n "CREATE: \"/media\" symlink.. "
        ln -s "${PZ_HOME}/media" "$MEDIA_DIR"
        echo "DONE."
    fi
}

create_mods_symlink () {
    if [ -L "$MODS_DIR" ]; then
        echo -n "EXISTS: \"/mods\" "
    else
        echo -n "Creating \"/mods\" symlink.. "
        ln -s "${PZ_HOME}/mods" "${MODS_DIR}"
        echo "DONE."
    fi
}

check_pzhome
create_media_symlink
create_mods_symlink
