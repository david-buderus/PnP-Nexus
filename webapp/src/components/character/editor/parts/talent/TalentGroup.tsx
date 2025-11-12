import {useNode} from '@craftjs/core';
import {Button, Card, Divider, Group, Modal, Stack, Table, TextInput} from '@mantine/core';
import {getPartStyle, TABLE_ROW_HEIGHT, TABLE_STYLE} from '../Constants';
import React, {useContext, useMemo, useState} from 'react';
import {PnPCharacterContext} from '../../../PnPCharacterContext';
import {Talent} from '../../../../../api';
import {useTranslation} from 'react-i18next';
import {useDisclosure} from '@mantine/hooks';
import {ObjectMultiSelect, PrimaryAttributeSelect} from '../../../../input/ObjectSelect';
import {fetchAllPrimaryAttributes, fetchAllTalents} from '../../../../Database';
import {toIdMap} from '../../../../utils/Utils';
import {AddableOrderModifier} from '../OrderModifier';

/** Shows level and co of the character */
export const TalentGroup = ({
    groupName,
    talentIds,
    firstAttributeId,
    secondAttributeId,
    thirdAttributeId
}: {
    groupName: string;
    talentIds?: string[];
    firstAttributeId?: string;
    secondAttributeId?: string;
    thirdAttributeId?: string;
}) => {
    const {character} = useContext(PnPCharacterContext);
    const {connectors: {connect, drag}, selected} = useNode((state => ({
        selected: state.events.selected
    })));

    const [primaryAttributes] = fetchAllPrimaryAttributes();
    const attributeMap = toIdMap(primaryAttributes);
    const [talents] = fetchAllTalents();
    const talentsMap = toIdMap(talents);

    let talentOrder: Talent[] = [];
    if (talentIds) {
        talentOrder = talentIds.map(id => talentsMap[id]);
    }

    const attributeString = useMemo(() => {
        let result = '';

        if (firstAttributeId && attributeMap[firstAttributeId]) {
            result = attributeMap[firstAttributeId].shortName;
        }
        if (secondAttributeId && attributeMap[secondAttributeId]) {
            if (result.length > 0) {
                result += ' / ';
            }
            result += attributeMap[secondAttributeId].shortName;
        }
        if (thirdAttributeId && attributeMap[thirdAttributeId]) {
            if (result.length > 0) {
                result += ' / ';
            }
            result += attributeMap[thirdAttributeId].shortName;
        }

        return result;

    }, [firstAttributeId, secondAttributeId, thirdAttributeId, attributeMap]);

    return <Table
        withTableBorder
        withColumnBorders
        variant="vertical"
        ref={ref => connect(drag(ref))}
        style={{...getPartStyle(selected), tableLayout: 'fixed'}}
    >
        <Table.Tbody>
            <Table.Tr h={TABLE_ROW_HEIGHT}>
                <Table.Th colSpan={2} h={TABLE_ROW_HEIGHT} style={{
                    width: '40%',
                    textAlign: 'center',
                    fontWeight: 'bold',
                    ...TABLE_STYLE
                }}>
                    {groupName ?? ''}
                </Table.Th>
                <Table.Th style={{width: '30%', fontWeight: 'bold', textAlign: 'center', ...TABLE_STYLE}}>
                    {attributeString}
                </Table.Th>
                <Table.Th style={{width: '30%', fontWeight: 'bold', textAlign: 'center', ...TABLE_STYLE}}/>
            </Table.Tr>

            {talentOrder.map((talent, index) => {
                if (!talent) {
                    return <Table.Tr key={'empty-row-' + index} h={TABLE_ROW_HEIGHT}>
                        <Table.Th style={{width: '30%', textAlign: 'center', ...TABLE_STYLE}}/>
                        <Table.Th style={{width: '10%', textAlign: 'center', ...TABLE_STYLE}}/>
                        <Table.Td style={{width: '30%', textAlign: 'center', ...TABLE_STYLE}}/>
                        <Table.Td style={{width: '30%', textAlign: 'center', ...TABLE_STYLE}}/>
                    </Table.Tr>;
                }

                return <Table.Tr key={talent.id} h={TABLE_ROW_HEIGHT}>
                    <Table.Th style={{width: '30%', textAlign: 'center', ...TABLE_STYLE}}>{talent.name}</Table.Th>
                    <Table.Th style={{width: '10%', textAlign: 'center', ...TABLE_STYLE}}></Table.Th>
                    <Table.Td style={{width: '30%', textAlign: 'center', ...TABLE_STYLE}}>
                        {
                            (talent.firstAttribute?.shortName ?? '??') + ' / ' +
                            (talent.secondAttribute?.shortName ?? '??') + ' / ' +
                            (talent.thirdAttribute?.shortName ?? '??')
                        }
                    </Table.Td>
                    <Table.Td style={{width: '30%', textAlign: 'center', ...TABLE_STYLE}}>
                        {character?.talents[talent.id]?.totalValue ?? 0}
                    </Table.Td>
                </Table.Tr>;
            })}
        </Table.Tbody>
    </Table>;
};

const TalentGroupSettings = () => {
    const {t} = useTranslation();
    const {
        actions: {setProp},
        talentIds,
        groupName,
        firstAttributeId,
        secondAttributeId,
        thirdAttributeId
    } = useNode(node => ({
        talentIds: node.data.props.talentIds,
        groupName: node.data.props.groupName,
        firstAttributeId: node.data.props.firstAttributeId,
        secondAttributeId: node.data.props.secondAttributeId,
        thirdAttributeId: node.data.props.thirdAttributeId
    }));
    const [talents] = fetchAllTalents();
    const talentsOrder: string[] = talentIds ?? [];
    console.log(talentsOrder);

    const [primaryAttributes] = fetchAllPrimaryAttributes();
    const attributeMap = toIdMap(primaryAttributes);

    return <Stack>
        <TextInput
            label={t('name')}
            value={groupName}
            onChange={e => {
                setProp(props => props.groupName = e.currentTarget.value);
            }}
        />
        <PrimaryAttributeSelect
            label={t('character:firstAttribute')}
            value={attributeMap[firstAttributeId]}
            onChange={a => {
                setProp(props => props.firstAttributeId = a?.id);
            }}
            clearable
        />
        <PrimaryAttributeSelect
            label={t('character:secondAttribute')}
            value={attributeMap[secondAttributeId]}
            onChange={a => {
                setProp(props => props.secondAttributeId = a?.id);
            }}
            clearable
        />
        <PrimaryAttributeSelect
            label={t('character:thirdAttribute')}
            value={attributeMap[thirdAttributeId]}
            onChange={a => {
                setProp(props => props.thirdAttributeId = a?.id);
            }}
            clearable
        />
        <Divider/>
        <AddableOrderModifier
            order={talentsOrder}
            setOrder={order => {
                setProp(props => props.talentIds = order);
            }}
            fullList={talents}
            addDialog={add => <AddTalentDialog addTalent={add}/>}
        />
    </Stack>;
};

function AddTalentDialog({
    addTalent
}: {
    addTalent: (t: Talent | Talent[]) => void;
}) {
    const {t} = useTranslation();
    const [talents] = fetchAllTalents();
    const [opened, {open, close}] = useDisclosure(false);
    const [importTalents, setImportTalents] = useState<Talent[]>([]);

    return <>
        <Modal opened={opened} onClose={close} title={t('sheetEditor:addTalent')}>
            <Stack>
                <ObjectMultiSelect<Talent>
                    label={t('talents')}
                    value={importTalents}
                    onChange={setImportTalents}
                    data={talents}
                    idKey="id"
                    labelKey="name"
                />
                <Group justify="flex-end">
                    <Button autoFocus variant="outline" onClick={close}>
                        {t('cancel')}
                    </Button>
                    <Button type="submit" disabled={!importTalents} onClick={() => {
                        addTalent(importTalents);
                        close();
                    }}>
                        {t('confirm')}
                    </Button>
                </Group>
            </Stack>
        </Modal>
        <Card
            style={{cursor: 'pointer'}}
            shadow="sm"
            onClick={open}
        >
            {t('sheetEditor:addTalent')}
        </Card>
    </>;
}

TalentGroup.craft = {
    name: 'sheetEditor:talentGroup',
    related: {
        settings: TalentGroupSettings
    }
};