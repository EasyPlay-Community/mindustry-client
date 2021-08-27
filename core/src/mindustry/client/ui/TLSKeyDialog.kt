package mindustry.client.ui

import arc.*
import arc.input.*
import arc.scene.ui.*
import arc.scene.ui.layout.*
import mindustry.*
import mindustry.client.*
import mindustry.client.crypto.*
import mindustry.client.utils.*
import mindustry.gen.*
import mindustry.ui.*
import mindustry.ui.dialogs.*
import java.security.cert.*

class TLSKeyDialog : BaseDialog("@client.keyshare") {

    private val keys = Table()
    private lateinit var importDialog: Dialog
    private lateinit var aliasDialog: Dialog

    init {
        build()
    }

    private fun regenerate() {
        keys.clear()
        keys.defaults().pad(5f).left()
        val store = Main.keyStorage
        for (cert in store.trusted()) {
            val table = Table()
            table.button(Icon.cancel, Styles.emptyi, 16f) {
                store.untrust(cert)
                regenerate()
            }.padRight(20f).tooltip("@save.delete")

            table.button(Icon.edit, Styles.emptyi, 16f) button2@ {
                aliasDialog = dialog("@client.alias") {
                    val aliasInput = TextField("")
                    aliasInput.setFilter { _, c -> c.isLetterOrDigit() }
                    aliasInput.messageText = "@client.noalias"
                    cont.row(aliasInput).width(400f)

                    cont.row().table{ ta ->
                        ta.defaults().width(194f).pad(3f)
                        ta.button("@ok"){
                            if (aliasInput.text.isBlank()) {
                                store.removeAlias(cert)
                                regenerate()
                                return@button
                            }
                            if ((store.trusted().any { it.readableName.equals(aliasInput.text, true) }) || store.aliases().any { it.second.equals(aliasInput.text, true) }) {
                                Vars.ui.showInfoFade("@client.aliastaken")
                                return@button
                            }
                            store.alias(cert, aliasInput.text)
                            regenerate()
                            hide()
                        }

                        ta.button("@close") {
                            hide()
                        }
                    }
                }.show()
            }.padRight(10f).tooltip("@client.editcert")
            table.label(cert.readableName).padRight(10f)
            table.label("(${store.alias(cert) ?: "client.noalias".bundle()})").right()
            keys.row(table)
        }
    }

    private fun build() {
        val store = Main.keyStorage

        if (store.cert() == null || store.key() == null || store.chain() == null) {
            Vars.ui.showTextInput(
                "@client.certname.title",
                "@client.certname.text",
                Core.settings.getString("name", "")
            ) { text ->
                if (text.length < 2) {
                    hide()
                    return@showTextInput
                }
                if (text.asciiNoSpaces() != text) {
                    hide()
                    Vars.ui.showInfoFade("@client.keyprincipalspaces")
                    return@showTextInput
                }

                val key = genKey()
                val cert = genCert(key, null, text)

                store.cert(cert)
                store.key(key, listOf(cert))

                build()
            }
        }

        regenerate()

        cont.table{ t ->
            t.defaults().pad(5f)

            t.pane { pane ->
                pane.add(keys)
            }.growX()

            t.row()

            t.button("@client.importkey") {
                importDialog = dialog("@client.importkey") {
                    val keyInput = TextField("")
                    keyInput.messageText = "@client.key"
                    keyInput.setValidator { k -> k.isNotEmpty() }
                    cont.row(keyInput).width(400f)

                    cont.row().table{ ta ->
                        ta.defaults().width(194f).pad(3f)
                        ta.button("@client.importkey") button2@{
                            val factory = CertificateFactory.getInstance("X509")
                            val cert = factory.generateCertificate(keyInput.text.base64()?.inputStream() ?: return@button2) as? X509Certificate ?: return@button2
                            if (cert.readableName.asciiNoSpaces() != cert.readableName) {
                                Vars.ui.showInfoFade("@client.keyprincipalspaces")  // spaces break the commands
                                return@button2
                            }
//                            if (cert.basicConstraints != -1) {  // don't allow it to be a CA cert
//                                Vars.ui.showInfoFade("@client.evilcert")
//                                return@button2
//                            }
                            Vars.ui.showConfirm("@client.importKey", cert.subjectX500Principal.name) {
                                store.trust(cert)
                                regenerate()
                            }
                            hide()
                        }.disabled { !keyInput.isValid }

                        ta.button("@close") {
                            hide()
                        }
                    }
                }.show()
            }.growX().wrapLabel(false)

            t.row()

            t.button("@client.exportkey") {
                Core.app.clipboardText = store.cert()?.encoded?.base64() ?: return@button
                Toast(3f).add("@copied")
            }.growX().get().label.setWrap(false)

            t.row()

            t.button("@close") {
                hide()
            }.growX().get().label.setWrap(false)
        }
        keyDown {
            if(it == KeyCode.escape || it == KeyCode.back){
                if (this::importDialog.isInitialized && importDialog.isShown) Core.app.post(importDialog::hide) // This game hates being not dumb so this is needed
                if (this::aliasDialog.isInitialized && aliasDialog.isShown) Core.app.post(aliasDialog::hide) // Same as above
                else Core.app.post(this::hide)
            }
        }
    }
}
