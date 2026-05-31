import {CharacterInfo} from './character/CharacterInfo';
import {LevelInfo} from './character/LevelInfo';
import {CharacterDescriptionInfo, CharacterDescriptionSelectable} from './character/CharacterDesciptionInfo';
import {AdvantagesInfo} from './character/AdvantagesInfo';
import {PrimaryAttributeInfo} from './stats/PrimaryAttributeInfo';
import {SecondaryAttributeInfo} from './stats/SecondaryAttributeInfo';
import {PrimaryAttributeRow} from './stats/PrimaryAttributeRow';
import {WeaponList} from './items/WeaponList';
import {ArmorSlots} from './items/ArmorSlots';
import {JewelleryList} from './items/JewelleryList';
import {InventoryPart} from './items/InventoryPart';
import {CurrencyPart} from './items/CurrencyPart';
import {TalentGroup} from './talent/TalentGroup';
import {SpellList} from './spells/SpellList';
import {FreeTextPart} from './other/FreeTextPart';
import {TitlePart} from './other/TitlePart';
import {CustomText} from './custom/CustomText';
import {CustomTable, TableDefinition} from './custom/CustomTable';
import {TitleOrder} from '@mantine/core';

/** Data of a page element */
export type PageElementData = {
    type: 'CHARACTER_INFO';
} | {
    type: 'LEVEL_INFO';
} | {
    type: 'DESCRIPTION_INFO';
    description: CharacterDescriptionSelectable;
} | {
    type: 'ADVANTAGES_INFO';
    showsAdvantages: boolean;
} | {
    type: 'PRIMARY_ATTRIBUTE_INFO';
    attributesOrder?: string[]
} | {
    type: 'SECONDARY_ATTRIBUTE_INFO';
    attributesOrder?: string[]
} | {
    type: 'PRIMARY_ATTRIBUTE_ROW';
    attributesOrder?: string[]
} | {
    type: 'WEAPON_LIST';
    numberOfRows: number;
    numberOfFallbackRows: number;
    showShields: boolean;
} | {
    type: 'ARMOR_SLOTS';
    numberOfShieldRows: number;
} | {
    type: 'JEWELLERY_LIST';
    numberOfJewellery: Record<string, number>;
} | {
    type: 'INVENTORY';
    rows: number;
    columns: number;
} | {
    type: 'CURRENCY';
    withoutLabel: boolean;
    oneLine: boolean;
    showCurrency: string;
} | {
    type: 'TALENTS';
    groupName: string;
    talentIds?: string[];
    firstAttributeId?: string;
    secondAttributeId?: string;
    thirdAttributeId?: string;
} | {
    type: 'SPELL_LIST';
    numberOfRows: number;
} | {
    type: 'FREE_TEXT';
    text: string;
    fontSize: string;
} | {
    type: 'TITLE';
    text: string;
    order: TitleOrder;
} | {
    type: 'CUSTOM_TEXT';
    title: string;
    customId: string;
} | {
    type: 'CUSTOM_TABLE';
    definition: TableDefinition;
}

/** Layout of page element */
export type PageElementLayout = {
    i: string;
    x: number;
    y: number;
    w: number;
    h: number;
    minW: number;
    minH: number;
}

/** A single element on a character sheet page */
export function PageElement({
    data, setData
}: {
    data: PageElementData
    setData: (d: PageElementData) => void;
}) {
    switch (data.type) {
        case 'CHARACTER_INFO':
            return <CharacterInfo/>;
        case 'LEVEL_INFO':
            return <LevelInfo/>;
        case 'DESCRIPTION_INFO':
            return <CharacterDescriptionInfo
                description={data.description}
                setDescription={d => setData({...data, description: d})}
            />;
        case 'ADVANTAGES_INFO':
            return <AdvantagesInfo
                showsAdvantages={data.showsAdvantages}
                setShowsAdvantages={b => setData({...data, showsAdvantages: b})}
            />;
        case 'PRIMARY_ATTRIBUTE_INFO':
            return <PrimaryAttributeInfo
                attributesOrder={data.attributesOrder}
                setAttributesOrder={o => setData({...data, attributesOrder: o})}
            />;
        case 'SECONDARY_ATTRIBUTE_INFO':
            return <SecondaryAttributeInfo
                attributesOrder={data.attributesOrder}
                setAttributesOrder={o => setData({...data, attributesOrder: o})}
            />;
        case 'PRIMARY_ATTRIBUTE_ROW':
            return <PrimaryAttributeRow
                attributesOrder={data.attributesOrder}
                setAttributesOrder={o => setData({...data, attributesOrder: o})}
            />;
        case 'WEAPON_LIST':
            return <WeaponList
                numberOfRows={data.numberOfRows}
                numberOfFallbackRows={data.numberOfFallbackRows}
                showShields={data.showShields}
                setNumberOfRows={n => setData({...data, numberOfRows: n})}
                setNumberOfFallbackRows={n => setData({...data, numberOfFallbackRows: n})}
                setShowShields={b => setData({...data, showShields: b})}
            />;
        case 'ARMOR_SLOTS':
            return <ArmorSlots
                numberOfShieldRows={data.numberOfShieldRows}
                setNumberOfShieldRows={n => setData({...data, numberOfShieldRows: n})}
            />;
        case 'JEWELLERY_LIST':
            return <JewelleryList
                numberOfJewellery={data.numberOfJewellery}
                setNumberOfJewellery={j => setData({...data, numberOfJewellery: j})}
            />;
        case 'INVENTORY':
            return <InventoryPart
                rows={data.rows}
                columns={data.columns}
                setRows={j => setData({...data, rows: j})}
                setColumns={j => setData({...data, columns: j})}
            />;
        case 'CURRENCY':
            return <CurrencyPart
                withoutLabel={data.withoutLabel}
                oneLine={data.oneLine}
                showCurrency={data.showCurrency}
                setWithoutLabel={b => setData({...data, withoutLabel: b})}
                setOneLine={b => setData({...data, oneLine: b})}
                setShowCurrency={s => setData({...data, showCurrency: s})}
            />;
        case 'TALENTS':
            return <TalentGroup
                groupName={data.groupName}
                talentIds={data.talentIds}
                firstAttributeId={data.firstAttributeId}
                secondAttributeId={data.secondAttributeId}
                thirdAttributeId={data.thirdAttributeId}
                setGroupName={s => setData({...data, groupName: s})}
                setTalentIds={ids => setData({...data, talentIds: ids})}
                setFirstAttributeId={s => setData({...data, firstAttributeId: s})}
                setSecondAttributeId={s => setData({...data, secondAttributeId: s})}
                setThirdAttributeId={s => setData({...data, thirdAttributeId: s})}
            />;
        case 'SPELL_LIST':
            return <SpellList
                numberOfRows={data.numberOfRows}
                setNumberOfRows={n => setData({...data, numberOfRows: n})}
            />;
        case 'FREE_TEXT':
            return <FreeTextPart
                text={data.text}
                fontSize={data.fontSize}
                setText={s => setData({...data, text: s})}
                setFontSize={s => setData({...data, fontSize: s})}
            />;
        case 'TITLE':
            return <TitlePart
                text={data.text}
                order={data.order}
                setText={s => setData({...data, text: s})}
                setOrder={o => setData({...data, order: o})}
            />;
        case 'CUSTOM_TEXT':
            return <CustomText
                title={data.title}
                customId={data.customId}
                setTitle={s => setData({...data, title: s})}
                setCustomId={s => setData({...data, customId: s})}
            />;
        case 'CUSTOM_TABLE':
            return <CustomTable
                definition={data.definition}
                setDefinition={d => setData({...data, definition: d})}
            />;
        default:
            return <></>;
    }
}