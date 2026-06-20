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
import {TableTextInput} from '../inputs/TableTextInput';
import {PnPCharacterPrintContext} from '../../../PnPCharacterPrintContext';

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
    const {showTalents} = useContext(PnPCharacterPrintContext);

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
            layout="fixed"
        >
            <colgroup>
                <col style={{width: '30%'}}/>
                <col style={{width: '10%'}}/>
                <col style={{width: '30%'}}/>
                <col style={{width: '30%'}}/>
            </colgroup>

            <Table.Tbody>
                <Table.Tr h={TABLE_ROW_HEIGHT}>
                    <Table.Th colSpan={2} h={TABLE_ROW_HEIGHT} style={{
                        textAlign: 'center',
                        fontWeight: 'bold',
                        ...TABLE_STYLE
                    }}>
                        {groupName ?? ''}
                    </Table.Th>
                    <Table.Th style={{fontWeight: 'bold', textAlign: 'center', ...TABLE_STYLE}}>
                        {attributeString}
                    </Table.Th>
                    <Table.Th style={{fontWeight: 'bold', textAlign: 'center', ...TABLE_STYLE}}>
                        <TableTalentRollInput
                            className={!showTalents ? 'no-print' : undefined}
                            allowDecimal={false}
                            allowNegative={false}
                            readOnly={!allowEdit}
                            disabled={!groupName}
                            key={characterForm.key(`customFields.${groupName}`)}
                            {...characterForm.getInputProps(`customFields.${groupName}`)}
                            value={characterForm.getInputProps(`customFields.${groupName}`).value || ''}
                        />
                    </Table.Th>
                </Table.Tr>

                {talentOrder.map((talent, index) => {
                    if (!talent) {
                        return <Table.Tr key={'empty-row-' + index} h={TABLE_ROW_HEIGHT}>
                            <Table.Th style={{textAlign: 'center', ...TABLE_STYLE}}/>
                            <Table.Th style={{textAlign: 'center', ...TABLE_STYLE}}/>
                            <Table.Td style={{textAlign: 'center', ...TABLE_STYLE}}/>
                            <Table.Td style={{textAlign: 'center', ...TABLE_STYLE}}/>
                        </Table.Tr>;
                    }

                    return <Table.Tr key={talent.id} h={TABLE_ROW_HEIGHT}>
                        <Table.Th style={{textAlign: 'center', ...TABLE_STYLE}}>{talent.name}</Table.Th>
                        <Table.Th style={{textAlign: 'center', ...TABLE_STYLE}}>
                            <TableTextInput
                                styles={{input: {textAlign: 'right'}}}
                                className={!showTalents ? 'no-print' : undefined}
                                key={characterForm.key(`customFields.${talent.id}`)}
                                {...characterForm.getInputProps(`customFields.${talent.id}`)}
                                value={characterForm.getInputProps(`customFields.${talent.id}`).value || ''}
                            />
                        </Table.Th>
                        <Table.Td style={{textAlign: 'center', ...TABLE_STYLE}}>
                            {
                                (talent.firstAttribute?.shortName ?? '??') + ' / ' +
                                (talent.secondAttribute?.shortName ?? '??') + ' / ' +
                                (talent.thirdAttribute?.shortName ?? '??')
                            }
                        </Table.Td>
                        <Table.Td style={{textAlign: 'center', ...TABLE_STYLE}}>
                            <TableTalentRollInput
                                className={!showTalents ? 'no-print' : undefined}
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