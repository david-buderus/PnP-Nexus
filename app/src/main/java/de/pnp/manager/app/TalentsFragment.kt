package de.pnp.manager.app

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import de.pnp.manager.app.model.PrimaryAttribute
import de.pnp.manager.app.state.DatabaseHelper
import de.pnp.manager.model.character.IPnPCharacter
import de.pnp.manager.model.other.ITalent

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class TalentsFragment(var character: IPnPCharacter) : Fragment() {

    private var lastLayout : View? = null

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_talents, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val header = Array(6) {
            when (it) {
                0 -> "Physical / Movement"
                1 -> "Praying"
                2 -> "Knowledge / Senses"
                3 -> "Socials"
                4 -> "Fighting"
                5 -> "Magic"
                else -> ""
            }
        }

        val physicalTalents = Array(29) {
            when(it) {
                0 -> "Abrollen"
                1 -> "Angeln"
                2 -> "Entfesseln"
                3 -> "Diebstahl"
                4 -> "Klettern"
                5 -> "Schleichen"
                6 -> "Laufen"
                7 -> "Verstecken"
                8 -> "Schwimmen"
                9 -> "Zielen"
                10 -> "Springen"
                11 -> "Werfen"
                12 -> "Tanzen"
                13 -> ""
                14 -> ""
                15 -> ""
                16 -> "Immunsystem"
                17 -> "Bergbau"
                18 -> "Schmerzempfinden"
                19 -> "Holz Hacken"
                20 -> "Körperliche Arbeit"
                21 -> "Jagen"
                22 -> ""
                23 -> ""
                24 -> "Reiten"
                25 -> "Rudern"
                26 -> "Kutschefahren"
                27 -> "Schifffahren"
                28 -> "Fliegen"
                else -> ""
            }
        }

        val prayingTalents = Array(18) {
            when(it) {
                0 -> "Beten zu Eros"
                1 -> "Beten zu Doram"
                2 -> "Beten zu Loruza"
                3 -> "Beten zu Berasos"
                4 -> "Beten zu Pulano"
                5 -> "Beten zu Herador"
                6 -> "Beten zu Lehana"
                7 -> "Beten zu Utason"
                8 -> "Beten zu Amure"
                9 -> "Beten zu Holaran"
                10 -> ""
                11 -> "Beten zu Grodan"
                12 -> ""
                13 -> ""
                14 -> "Beten zu Drudes"
                15 -> "Beten zu Ador"
                16 -> "Beten zu Drodan"
                17 -> "Beten zu Halan"
                else -> ""
            }
        }

        val knowledgeTalents = Array(40) {
            when(it) {
                0 -> "Astronomie / Wetter"
                1 -> "Gesellschaftskunde"
                2 -> "Götter / Kulte"
                3 -> "Gesetzeskunde"
                4 -> "Musikalisches Wissen"
                5 -> "Sprachkenntnisse"
                6 -> "Mechanisches Wissen"
                7 -> ""
                8 -> ""
                9 -> ""
                10 -> "Mathematik"
                11 -> "Zeichnen"
                12 -> "Biologie"
                13 -> "Schloss knacken"
                14 -> "Botanik"
                15 -> "Stimmen imitieren"
                16 -> "Giftkunde"
                17 -> ""
                18 -> ""
                19 -> ""
                20 -> "Fallenstellen"
                21 -> "Überlebenstechniken"
                22 -> "Knoten"
                23 -> "Orientierungssinn"
                24 -> "Kochen"
                25 -> "Spurenlesen"
                26 -> "Medizin"
                27 -> "Zähmen"
                28 -> "Alchemie"
                29 -> ""
                30 -> "Tüfteln / Handwerk"
                31 -> ""
                32 -> ""
                33 -> ""
                34 -> "Hören"
                35 -> "Sehen"
                36 -> "Riechen"
                37 -> "Suchen"
                38 -> "Schmecken"
                39 -> "Schätzen"
                else -> ""
            }
        }

        val socialTalents = Array(14) {
            when(it) {
                0 -> "Beruhigen"
                1 -> "Gassenwissen"
                2 -> "Betören"
                3 -> "Menschenkenntnis"
                4 -> "Betrügen"
                5 -> "Selbstbeherrschung"
                6 -> "Feilschen"
                7 -> ""
                8 -> "Lügen"
                9 -> "Singen"
                10 -> "Überzeugen / Begeistern / Führen"
                11 -> "Gaukeln"
                12 -> "Bedrohen / Einschüchtern"
                13 -> "Zechen"
                else -> ""
            }
        }

        val fightingTalents = Array(19) {
            when(it) {
                0 -> "Faustkampf"
                1 -> "Stangenwaffen"
                2 -> "Klingenwaffen Einhändig"
                3 -> "Stumpfe Waffen Einhändig"
                4 -> "Klingenwaffen Zweihändig"
                5 -> "Stumpfe Waffen Zweihändig"
                6 -> "Kleine Wurfwaffen"
                7 -> "Große Wurfwaffen"
                8 -> "Bogen / Armbrust"
                9 -> "Gewehr"
                10 -> "Improvisierte Waffen"
                11 -> ""
                12 -> ""
                13 -> "Ausweichen"
                14 -> "Gefahreninstinkt (Magier)"
                15 -> "Gefahreninstinkt (Normal)"
                16 -> "Parieren"
                17 -> "Entwaffnen"
                18 -> "Blocken"
                else -> ""
            }
        }

        val magicTalents = Array(16) {
            when(it) {
                0 -> "Magisches Wissen"
                1 -> "Verzauberung"
                2 -> ""
                3 -> ""
                4 -> "Arkanmagie"
                5 -> "Illusionsmagie"
                6 -> "Lichtmagie"
                7 -> "Finstermagie"
                8 -> "Feuermagie"
                9 -> "Wassermagie"
                10 -> "Luftmagie"
                11 -> "Erdmagie"
                12 -> "Sturmmagie"
                13 -> "Frostmagie"
                14 -> "Naturmagie"
                15 -> "Totenmagie"
                else -> ""
            }
        }

        val talentMap = HashMap<String, Collection<ITalent?>>()

        talentMap[header[0]] = physicalTalents.map {
            if (it.isBlank()) null else DatabaseHelper.getTalent(it) }
        talentMap[header[1]] = prayingTalents.map {
            if (it.isBlank()) null else DatabaseHelper.getTalent(it) }
        talentMap[header[2]] = knowledgeTalents.map {
            if (it.isBlank()) null else DatabaseHelper.getTalent(it) }
        talentMap[header[3]] = socialTalents.map {
            if (it.isBlank()) null else DatabaseHelper.getTalent(it) }
        talentMap[header[4]] = fightingTalents.map {
            if (it.isBlank()) null else DatabaseHelper.getTalent(it) }
        talentMap[header[5]] = magicTalents.map {
            if (it.isBlank()) null else DatabaseHelper.getTalent(it) }

        lastLayout = view.findViewById<ConstraintLayout>(R.id.talents_constraint)

        val newLayout = TableLayout(this.context)
        talentMap.forEach{createTable(it, newLayout)}

        newLayout.setColumnShrinkable(2, true)
        //newLayout.setColumnShrinkable(4, true)
        newLayout.setColumnStretchable(1, true)

        newLayout.id = View.generateViewId()

        val constraintSet = ConstraintSet()

        val layout = this.view!!.findViewById<ConstraintLayout>(R.id.talents_constraint)

        layout.addView(newLayout)
        constraintSet.clone(layout)
        constraintSet.connect(newLayout.id, ConstraintSet.TOP, lastLayout!!.id, if (lastLayout is ConstraintLayout) ConstraintSet.TOP else ConstraintSet.BOTTOM, 40)
        constraintSet.connect(newLayout.id, ConstraintSet.START, layout.id, ConstraintSet.START, 20)
        constraintSet.connect(newLayout.id, ConstraintSet.END, layout.id, ConstraintSet.END, 20)
        constraintSet.applyTo(layout)
    }

    private fun createTable(entry: Map.Entry<String, Collection<ITalent?>>, newLayout : TableLayout) {
        val list = ArrayList(entry.value)

        val headerTextView = TextView(this.context)
        headerTextView.text = entry.key
        headerTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)

        val row = TableRow(this.context)
        val params: TableRow.LayoutParams = TableRow.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        params.setMargins(0, 30, 0, 10)
        row.layoutParams = params
        row.addView(headerTextView, params)
        newLayout.addView(row)

        for (i in list.indices) {
            val talent = list[i]
            val textView = TextView(this.context)
            textView.text = talent?.name?:""

            val textView2 = TextView(this.context)
            textView2.text = if (talent != null) talent.attributes.map {
                when(it) {
                    PrimaryAttribute.agility -> "BW"
                    PrimaryAttribute.charisma -> "CH"
                    PrimaryAttribute.dexterity -> "GE"
                    PrimaryAttribute.endurance -> "AU"
                    PrimaryAttribute.intelligence -> "IN"
                    PrimaryAttribute.precision -> "GN"
                    PrimaryAttribute.resilience -> "BL"
                    PrimaryAttribute.strength -> "KK"
                    else -> ""
                }
            }.toString() else ""

            val textView3 = TextView(this.context)
            textView3.text = if (talent != null) character.talents[talent].toString() else ""

            val row = TableRow(this.context)
            row.addView(textView)
            row.addView(textView2)
            row.addView(textView3)

            val middleTextView = TextView(this.context)
            middleTextView.text = ""
            row.addView(middleTextView)

            newLayout.addView(row)
        }
    }


}