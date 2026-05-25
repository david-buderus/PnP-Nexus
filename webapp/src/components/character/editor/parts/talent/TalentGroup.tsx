import {Divider, Stack, Table, TextInput} from '@mantine/core';
import {TABLE_ROW_HEIGHT, TABLE_STYLE} from '../Constants';
import React, {useContext, useMemo} from 'react';
import {PnPCharacterContext} from '../../../PnPCharacterContext';
import {Talent} from '../../../../../api/model';
import {useTranslation} from 'react-i18next';
import {PrimaryAttributeSelect} from '../../../../input/ObjectSelect';
import {fetchAllPrimaryAttributes, fetchAllTalents} from '../../../../Database';
import {toIdMap} from '../../../../utils/Utils';
import {AddableOrderModifier} from '../OrderModifier';
import {TableTalentRollInput} from '../inputs/TableTalentRollInput';
import {PageElementSettings} from '../PageElementSettings';

/** Props of the TalentGroup */
export type TalentGroupProps = {
    groupName: string;
    talentIds?: string[];
    firstAttributeId?: string;
    secondAttributeId?: string;
    thirdAttributeId?: string;
    setGroupName: (s: string) => void;
    setTalentIds: (ids: string[]) => void;
    setFirstAttributeId: (s: string) => void;
    setSecondAttributeId: (s: string) => void;
    setThirdAttributeId: (s: string) => void;
}

/** Shows level and co of the character */
export function TalentGroup({
    groupName,
    talentIds,
    firstAttributeId,
    secondAttributeId,
    thirdAttributeId,
    setGroupName,
    setTalentIds,
    setFirstAttributeId,
    setSecondAttributeId,
    setThirdAttributeId
}: TalentGroupProps) {
    const {characterForm, allowEdit} = useContext(PnPCharacterContext);

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

    return <>
        <Table
            withTableBorder
            withColumnBorders
            variant="vertical"
            style={{tableLayout: 'fixed'}}
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
                            <TableTalentRollInput
                                allowDecimal={false}
                                allowNegative={false}
                                readOnly={!allowEdit}
                                key={characterForm.key(`talents.${talent.id}`)}
                                {...characterForm.getInputProps(`talents.${talent.id}`)}
                            />
                        </Table.Td>
                    </Table.Tr>;
                })}
            </Table.Tbody>
        </Table>
        <PageElementSettings>
            <TalentGroupSettings
                groupName={groupName}
                talentIds={talentIds}
                firstAttributeId={firstAttributeId}
                secondAttributeId={secondAttributeId}
                thirdAttributeId={thirdAttributeId}
                setGroupName={setGroupName}
                setTalentIds={setTalentIds}
                setFirstAttributeId={setFirstAttributeId}
                setSecondAttributeId={setSecondAttributeId}
                setThirdAttributeId={setThirdAttributeId}
            />
        </PageElementSettings>
    </>;
}

function TalentGroupSettings({
    groupName,
    talentIds,
    firstAttributeId,
    secondAttributeId,
    thirdAttributeId,
    setGroupName,
    setTalentIds,
    setFirstAttributeId,
    setSecondAttributeId,
    setThirdAttributeId
}: TalentGroupProps) {
    const {t} = useTranslation();
    const [talents] = fetchAllTalents();
    const talentsOrder: string[] = talentIds ?? [];

    const [primaryAttributes] = fetchAllPrimaryAttributes();
    const attributeMap = toIdMap(primaryAttributes);

    return <Stack>
        <TextInput
            label={t('name')}
            value={groupName}
            onChange={e => setGroupName(e.currentTarget.value)}
        />
        <PrimaryAttributeSelect
            label={t('character:firstAttribute')}
            value={attributeMap[firstAttributeId]}
            onChange={a => setFirstAttributeId(a?.id)}
            clearable
            comboboxProps={{withinPortal: false}}
        />
        <PrimaryAttributeSelect
            label={t('character:secondAttribute')}
            value={attributeMap[secondAttributeId]}
            onChange={a => setSecondAttributeId(a?.id)}
            clearable
            comboboxProps={{withinPortal: false}}
        />
        <PrimaryAttributeSelect
            label={t('character:thirdAttribute')}
            value={attributeMap[thirdAttributeId]}
            onChange={a => setThirdAttributeId(a?.id)}
            clearable
            comboboxProps={{withinPortal: false}}
        />
        <Divider/>
        <AddableOrderModifier
            order={talentsOrder}
            setOrder={setTalentIds}
            fullList={talents}
        />
    </Stack>;
}