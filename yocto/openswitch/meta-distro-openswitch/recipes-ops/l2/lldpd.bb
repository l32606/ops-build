
SUMMARY = "OpenSwitch LLDP Daemon"
LICENSE = "ISC"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "ops-utils config-yaml ops-ovsdb libevent openssl"

SRC_URI = "git://git.openhalon.io/openhalon/lldpd;protocol=http \
	  file://lldpd.service \
"

SRCREV="${AUTOREV}"

# When using AUTOREV, we need to force the package version to the revision of git
# in order to avoid stale shared states.
PV = "git${SRCPV}"

S = "${WORKDIR}/git"

inherit openswitch autotools systemd pkgconfig

# Autoreconf breaks on
# gnu-configize: `configure.ac' or `configure.in' is required
#
# Works good enough without autoreconf
do_configure() {
	cd ${S}
        autoreconf -fi
        cd ${B}
	oe_runconf
}

# Disable readline to skip GPL linking
EXTRA_OECONF = "--enable-ovsdb --disable-privsep --without-readline"

do_install_append() {
     install -d ${D}${systemd_unitdir}/system
     install -m 0644 ${WORKDIR}/lldpd.service ${D}${systemd_unitdir}/system/
}

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE_${PN} = "lldpd.service"

FILES_${PN} += "/usr/share/zsh usr/lib/sysusers.d"
