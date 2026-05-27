import {ActionIcon, Group, NumberInput, Popover, Stack, Table, Text} from '@mantine/core';
import {TABLE_ROW_HEIGHT, TABLE_STYLE} from '../Constants';
import React, {useContext, useMemo, useState} from 'react';
import {useTranslation} from 'react-i18next';
import {PnPCharacterContext} from '../../../PnPCharacterContext';
import {Jewellery, JewelleryDefinition, JewelleryEquipment} from '../../../../../api/model';
import {useUniverseContext} from '../../../../PageBase';
import {PageElementSettings} from '../PageElementSettings';
import {fetchAllJewllery} from '../../../../Database';
import {IconCircleMinus, IconCirclePlus} from '@tabler/icons-react';
import {ItemSearchCard} from '../../../../items/ItemSearchCard';
import {UpgradePopover} from '../../../../items/UpgradeControl';
import {ItemStackCardModal} from '../../../../items/ItemStackCard';
import {useCreateJewellery} from '../../../../../api/item-stack-service/item-stack-service';
import {handleNetworkErrors} from '../../../../utils/ErrorUtils';

/** Shows jewellery of the character */
export function JewelleryList({numberOfJewellery, setNumberOfJewellery}: {
    numberOfJewellery: Record<string, number>;
    setNumberOfJewellery: (j: Record<string, number>) => void;
}) {
    const {t} = useTranslation();
    const {equipmentSettings} = useUniverseContext();

    return <>
        <Table
            withTableBorder
            withColumnBorders
            striped
        >
            <Table.Tbody>
                <Table.Tr h={TABLE_ROW_HEIGHT}>
                    <Table.Th style={{width: '20%', ...TABLE_STYLE}}>{t('jewellery')}</Table.Th>
                    <Table.Th style={{width: '25%', ...TABLE_STYLE}}>{t('name')}</Table.Th>
                    <Table.Th style={{width: '55%', ...TABLE_STYLE}}>{t('effect')}</Table.Th>
                </Table.Tr>
                {equipmentSettings.jewelleryDefinitions.map(definition => <JewelleryLines
                    key={definition.name}
                    definition={definition}
                    numberOfJewellery={numberOfJewellery}
                />)}
            </Table.Tbody>
        </Table>
        <PageElementSettings>
            <Stack>
                {equipmentSettings.jewelleryDefinitions.map(definition =>
                    <NumberInput
                        key={definition.name}
                        label={definition.name}
                        value={numberOfJewellery[definition.name]}
                        onChange={e => {
                            const changed = {...numberOfJewellery};
                            changed[definition.name] = Number(e);
                            setNumberOfJewellery(changed);
                        }}
                        min={1}
                        max={definition.amount}
                        allowDecimal={false}
                    />
                )}
            </Stack>
        </PageElementSettings>
    </>;
}

function JewelleryLines({
    definition,
    numberOfJewellery
}: {
    definition: JewelleryDefinition,
    numberOfJewellery: Record<string, number>
}) {
    const {characterForm, allowEdit} = useContext(PnPCharacterContext);
    const character = characterForm.getValues();

    const number = numberOfJewellery[definition.name];
    const jewellery = useMemo(() => {
        const list = character.equipment.jewellery[definition.name] ?? [];
        const w = [...list];
        w.length = number ?? 1;
        w.fill(null, list.length);
        return w;
    }, [character.equipment, number]);

    const [lastClicked, setLastClicked] = useState<JewelleryEquipment>(null);

    return <>
        {jewellery.map((j, index) => {

            let effect = j?.item.effects.map(e => e.description).join(',') ?? '';
            if (j?.upgradeSlots > 0) {
                effect += ` ${j.remainingUpgradeSlots}/${j.upgradeSlots} ${j.upgrades.map(u => u.name).join(', ')}`;
            }

            return <Table.Tr
                key={index}
                h={TABLE_ROW_HEIGHT}
                onClick={allowEdit ? () => setLastClicked(j ?? null) : null}
            >
                <Table.Td style={TABLE_STYLE}>{definition.name + (number > 1 ? ` ${index + 1}` : '')}</Table.Td>
                <Table.Td style={TABLE_STYLE}>{j?.item.name ?? ''}</Table.Td>
                <Table.Td style={TABLE_STYLE}>
                    <Group justify="space-between" wrap="nowrap" style={{width: '100%'}}>
                        <Text
                            size={TABLE_STYLE.fontSize}
                            truncate="end"
                            style={{flex: 1, minWidth: 0}}
                        >
                            {effect}
                        </Text>
                        {j ?
                            allowEdit ?
                                <Group
                                    wrap="nowrap"
                                    gap={1}
                                    style={{flexShrink: 0}}
                                    onClick={e => e.stopPropagation()}
                                >
                                    <UpgradePopover
                                        item={j}
                                        onChange={e => characterForm.replaceListItem(`equipment.jewellery.${definition.name}`, index, e)}
                                    />
                                    <ActionIcon
                                        variant="subtle"
                                        size={TABLE_ROW_HEIGHT - 8}
                                        className="no-drag"
                                        onClick={() => characterForm.removeListItem(`equipment.jewellery.${definition.name}`, index)}
                                    >
                                        <IconCircleMinus color="red" size={14}/>
                                    </ActionIcon>
                                </Group> : null
                            :
                            <JewelleryAdditionPopover name={definition.name} tag={definition.tag}/>
                        }
                    </Group>
                </Table.Td>
            </Table.Tr>;
        })}
        <ItemStackCardModal stack={lastClicked} onClose={() => setLastClicked(null)}/>
    </>;
}

function JewelleryAdditionPopover({name, tag}: {
    name: string;
    tag: string;
}) {
    const {allowEdit, characterForm} = useContext(PnPCharacterContext);
    const [items] = fetchAllJewllery();
    const filtered = useMemo(() => items.filter(i => i.tags.includes(tag)), [items, tag]);
    const {mutate: createJewellery} = useCreateJewellery({
        mutation: {
            onSuccess: response => {
                if (!characterForm.values.equipment.jewellery[name]) {
                    characterForm.setFieldValue(`equipment.jewellery.${name}`, [response.data]);
                } else {
                    characterForm.insertListItem(`equipment.jewellery.${name}`, response.data);
                }
            },
            onError: handleNetworkErrors
        }
    });

    if (!allowEdit) {
        return null;
    }

    return (
        <Popover position="bottom" withArrow shadow="md">
            <Popover.Target>
                <ActionIcon
                    variant="subtle"
                    size={TABLE_ROW_HEIGHT - 8}
                    className="no-drag"
                >
                    <IconCirclePlus size={14}/>
                </ActionIcon>
            </Popover.Target>
            <Popover.Dropdown>
                <ItemSearchCard
                    items={filtered}
                    onSelect={item => createJewellery({
                        data: {item: item.item as Jewellery, stackSize: 1}
                    })}
                />
            </Popover.Dropdown>
        </Popover>
    );
}