import {useTranslation} from 'react-i18next';
import {Accordion, Button, Stack, Title} from '@mantine/core';
import React from 'react';
import {useUniverseContext} from '../../PageBase';
import {SHOW_ALL_CURRENCIES} from './parts/items/CurrencyPart';
import {PageElementData, PageElementLayout} from './parts/PageElement';
import {EMPTY_TABLE_DEFINITION} from './parts/custom/CustomTable';

/** Toolbox for the character sheet editor */
export function Toolbox({
    setActiveDropSettings
}: {
    setActiveDropSettings: (l: Partial<PageElementLayout>) => void,
}) {
    const {t} = useTranslation();
    const {equipmentSettings} = useUniverseContext();

    function createDragFunction(data: PageElementData, layout: Partial<PageElementLayout>) {
        return (e: React.DragEvent) => {
            e.dataTransfer.setData('design-element', JSON.stringify(data));
            setActiveDropSettings(layout);
        };
    }

    return <Stack>
        <Title order={3}>
            {t('sheetEditor:dragToAdd')}
        </Title>
        <Accordion>
            <Accordion.Item value="character">
                <Accordion.Control>{t('character')}</Accordion.Control>
                <Accordion.Panel>
                    <Stack gap="xs">
                        <Button
                            className="droppable-element"
                            draggable={true}
                            unselectable="on"
                            onDragStart={createDragFunction({type: 'CHARACTER_INFO'}, {minW: 3, minH: 3, w: 6, h: 3})}
                        >
                            {t('sheetEditor:characterInfo')}
                        </Button>
                        <Button
                            className="droppable-element"
                            draggable={true}
                            unselectable="on"
                            onDragStart={createDragFunction({type: 'LEVEL_INFO'}, {minW: 3, minH: 3, w: 6, h: 3})}
                        >
                            {t('sheetEditor:levelInfo')}
                        </Button>
                        <Button
                            className="droppable-element"
                            draggable={true}
                            unselectable="on"
                            onDragStart={createDragFunction({
                                type: 'DESCRIPTION_INFO', description: {
                                    id: 'appearance',
                                    name: t('character:appearance')
                                }
                            }, {minW: 3, minH: 3, w: 6, h: 3})}
                        >
                            {t('sheetEditor:characterDescription')}
                        </Button>
                        <Button
                            className="droppable-element"
                            draggable={true}
                            unselectable="on"
                            onDragStart={createDragFunction({
                                type: 'ADVANTAGES_INFO', showsAdvantages: true
                            }, {minW: 3, minH: 6, w: 6, h: 12})}
                        >
                            {t('advantages')}
                        </Button>
                    </Stack>
                </Accordion.Panel>
            </Accordion.Item>
            <Accordion.Item value="stats">
                <Accordion.Control>{t('sheetEditor:stats')}</Accordion.Control>
                <Accordion.Panel>
                    <Stack gap="xs">
                        <Button
                            className="droppable-element"
                            draggable={true}
                            unselectable="on"
                            onDragStart={createDragFunction({
                                type: 'PRIMARY_ATTRIBUTE_INFO'
                            }, {minW: 3, minH: 3, w: 6, h: 6})}
                        >
                            {t('sheetEditor:primaryAttributeInfo')}
                        </Button>
                        <Button
                            className="droppable-element"
                            draggable={true}
                            unselectable="on"
                            onDragStart={createDragFunction({
                                type: 'SECONDARY_ATTRIBUTE_INFO'
                            }, {minW: 3, minH: 3, w: 6, h: 6})}
                        >
                            {t('sheetEditor:secondaryAttributeInfo')}
                        </Button>
                        <Button
                            className="droppable-element"
                            draggable={true}
                            unselectable="on"
                            onDragStart={createDragFunction({
                                type: 'PRIMARY_ATTRIBUTE_ROW'
                            }, {minW: 3, minH: 2, w: 12, h: 2})}
                        >
                            {t('sheetEditor:primaryAttributeRow')}
                        </Button>
                    </Stack>
                </Accordion.Panel>
            </Accordion.Item>
            <Accordion.Item value="items">
                <Accordion.Control>{t('items')}</Accordion.Control>
                <Accordion.Panel>
                    <Stack gap="xs">
                        <Button
                            className="droppable-element"
                            draggable={true}
                            unselectable="on"
                            onDragStart={createDragFunction({
                                type: 'WEAPON_LIST',
                                numberOfWeapons: equipmentSettings?.numberOfHandheld ?? 2,
                                numberOfShields: 0
                            }, {minW: 3, minH: 3, w: 12, h: 3})}
                        >
                            {t('sheetEditor:weaponList')}
                        </Button>
                        <Button
                            className="droppable-element"
                            draggable={true}
                            unselectable="on"
                            onDragStart={createDragFunction({
                                type: 'ARMOR_SLOTS',
                                numberOfShieldRows: 0
                            }, {minW: 5, minH: 8, w: 6, h: 8})}
                        >
                            {t('sheetEditor:armorSlots')}
                        </Button>
                        <Button
                            className="droppable-element"
                            draggable={true}
                            unselectable="on"
                            onDragStart={createDragFunction({
                                type: 'JEWELLERY_LIST',
                                numberOfJewellery: equipmentSettings?.jewelleryDefinitions
                                    .reduce((acc, item) => {
                                        acc[item.name] = item.amount;
                                        return acc;
                                    }, {} as Record<string, number>)
                            }, {minW: 5, minH: 3, w: 6, h: 8})}
                        >
                            {t('sheetEditor:jewelleryList')}
                        </Button>
                        <Button
                            className="droppable-element"
                            draggable={true}
                            unselectable="on"
                            onDragStart={createDragFunction({
                                type: 'INVENTORY',
                                rows: 8,
                                columns: 5,
                            }, {minW: 3, minH: 3, w: 12, h: 8})}
                        >
                            {t('inventory')}
                        </Button>
                        <Button
                            className="droppable-element"
                            draggable={true}
                            unselectable="on"
                            onDragStart={createDragFunction({
                                type: 'CURRENCY',
                                withoutLabel: false,
                                oneLine: false,
                                showCurrency: SHOW_ALL_CURRENCIES
                            }, {minW: 1, minH: 1, w: 6, h: 3})}
                        >
                            {t('sheetEditor:currency')}
                        </Button>
                    </Stack>
                </Accordion.Panel>
            </Accordion.Item>
            <Accordion.Item value="talents">
                <Accordion.Control>{t('talents')}</Accordion.Control>
                <Accordion.Panel>
                    <Stack gap="xs">
                        <Button
                            className="droppable-element"
                            draggable={true}
                            unselectable="on"
                            onDragStart={createDragFunction({
                                type: 'TALENTS',
                                groupName: '',
                                talentIds: [],
                                firstAttributeId: '',
                                secondAttributeId: '',
                                thirdAttributeId: ''
                            }, {minW: 3, minH: 3, w: 6, h: 8})}
                        >
                            {t('sheetEditor:talentGroup')}
                        </Button>
                    </Stack>
                </Accordion.Panel>
            </Accordion.Item>
            <Accordion.Item value="spells">
                <Accordion.Control>{t('spells')}</Accordion.Control>
                <Accordion.Panel>
                    <Stack gap="xs">
                        <Button
                            className="droppable-element"
                            draggable={true}
                            unselectable="on"
                            onDragStart={createDragFunction({
                                type: 'SPELL_LIST',
                                numberOfRows: 8
                            }, {minW: 3, minH: 3, w: 12, h: 8})}
                        >
                            {t('sheetEditor:spellList')}
                        </Button>
                    </Stack>
                </Accordion.Panel>
            </Accordion.Item>
            <Accordion.Item value="custom">
                <Accordion.Control>{t('sheetEditor:customFields')}</Accordion.Control>
                <Accordion.Panel>
                    <Stack gap="xs">
                        <Button
                            className="droppable-element"
                            draggable={true}
                            unselectable="on"
                            onDragStart={createDragFunction({
                                type: 'CUSTOM_TEXT',
                                title: '',
                                customId: Date.now().toString(),
                            }, {minW: 3, minH: 3, w: 6, h: 3})}
                        >
                            {t('sheetEditor:textField')}
                        </Button>
                        <Button
                            className="droppable-element"
                            draggable={true}
                            unselectable="on"
                            onDragStart={createDragFunction({
                                type: 'CUSTOM_TABLE',
                                definition: EMPTY_TABLE_DEFINITION,
                            }, {minW: 3, minH: 3, w: 6, h: 3})}
                        >
                            {t('sheetEditor:table')}
                        </Button>
                    </Stack>
                </Accordion.Panel>
            </Accordion.Item>
            <Accordion.Item value="other">
                <Accordion.Control>{t('other')}</Accordion.Control>
                <Accordion.Panel>
                    <Stack gap="xs">
                        <Button
                            className="droppable-element"
                            draggable={true}
                            unselectable="on"
                            onDragStart={createDragFunction({
                                type: 'TITLE',
                                text: '',
                                order: 1,
                            }, {minW: 1, minH: 1, w: 12, h: 2})}
                        >
                            {t('sheetEditor:title')}
                        </Button>
                        <Button
                            className="droppable-element"
                            draggable={true}
                            unselectable="on"
                            onDragStart={createDragFunction({
                                type: 'FREE_TEXT',
                                text: '',
                                fontSize: 'md',
                            }, {minW: 1, minH: 1, w: 6, h: 3})}
                        >
                            {t('sheetEditor:freeText')}
                        </Button>
                    </Stack>
                </Accordion.Panel>
            </Accordion.Item>
        </Accordion>
    </Stack>;
}