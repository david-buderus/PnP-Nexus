import {Group, Select, Stack, Switch, Table} from '@mantine/core';
import {TABLE_ROW_HEIGHT, TABLE_STYLE} from '../Constants';
import React, {useContext, useEffect, useMemo, useState} from 'react';
import {useTranslation} from 'react-i18next';
import {PnPCharacterContext} from '../../../PnPCharacterContext';
import {useUniverseContext} from '../../../../PageBase';
import {getCurrencyCalculationSteps, splitCurrency} from '../../../../utils/Utils';
import {PageElementSettings} from '../PageElementSettings';
import {TableNumberInput} from '../inputs/TableNumberInput';
import {PnPCharacterPrintContext} from '../../../PnPCharacterPrintContext';

/** Constant to show all currencies */
export const SHOW_ALL_CURRENCIES = '__ALL_CURRENCIES__';

/** Shows the currency of the character */
export function CurrencyPart({
    withoutLabel,
    oneLine,
    showCurrency,
    setWithoutLabel,
    setOneLine,
    setShowCurrency
}: {
    withoutLabel: boolean;
    oneLine: boolean;
    showCurrency: string;
    setWithoutLabel: (b: boolean) => void;
    setOneLine: (b: boolean) => void;
    setShowCurrency: (s: string) => void;
}) {
    const {currencySettings} = useUniverseContext();
    const {characterForm} = useContext(PnPCharacterContext);
    const character = characterForm.getValues();
    const coins = useMemo(() => splitCurrency(currencySettings, character.inventory.coin),
        [currencySettings, character.inventory.coin]);
    const calculationSteps = useMemo(() => getCurrencyCalculationSteps(currencySettings),
        [currencySettings]);

    const [localCoins, setLocalCoins] = useState<number[]>([]);

    useEffect(() => {
        setLocalCoins(coins);
    }, [character.inventory.coin]);

    function handleLocalChange(index: number, newValue: number) {
        setLocalCoins(prev => {
            const next = [...prev];
            next[index] = newValue;
            return next;
        });
    }

    // Recalculate coins when user clicks away
    function handleBlur() {
        let total = 0;

        for (let i = 0; i < calculationSteps.length; i++) {
            const factor = calculationSteps[i];
            const coinIndex = calculationSteps.length - i - 1;

            const currentCoinValue = localCoins[coinIndex] ?? 0;
            total += factor * currentCoinValue;
        }

        characterForm.setFieldValue('inventory.coin', total);
    }

    return <>
        {oneLine ?
            <CurrencyOneLine
                withoutLabel={withoutLabel}
                localCoins={localCoins}
                handleLocalChange={handleLocalChange}
                handleBlur={handleBlur}
            />
            :
            <CurrencyMultiLine
                withoutLabel={withoutLabel}
                showCurrency={showCurrency}
                localCoins={localCoins}
                handleLocalChange={handleLocalChange}
                handleBlur={handleBlur}
            />
        }
        <PageElementSettings>
            <CurrencyPartSettings
                withoutLabel={withoutLabel}
                oneLine={oneLine}
                showCurrency={showCurrency}
                setWithoutLabel={setWithoutLabel}
                setOneLine={setOneLine}
                setShowCurrency={setShowCurrency}
            />
        </PageElementSettings>
    </>;
}

function CurrencyOneLine({
    withoutLabel,
    localCoins,
    handleLocalChange,
    handleBlur
}: {
    withoutLabel: boolean;
    localCoins: number[];
    handleBlur: () => void;
    handleLocalChange: (index: number, newValue: number) => void;
}) {
    const {t} = useTranslation();
    const {currencySettings} = useUniverseContext();
    const {allowEdit} = useContext(PnPCharacterContext);
    const {showItems} = useContext(PnPCharacterPrintContext);

    const reversedEntries = useMemo(() => {
            if (!currencySettings?.calculationEntries) {
                return [];
            }
            return [...currencySettings.calculationEntries].reverse();
        }, [currencySettings?.calculationEntries]
    );
    const totalEntriesCount = currencySettings.calculationEntries.length;

    return <Table variant="vertical" withTableBorder style={{width: '100%'}} layout="fixed">
        <Table.Tbody>
            <Table.Tr h={TABLE_ROW_HEIGHT}>
                {withoutLabel ? null : <Table.Th style={{...TABLE_STYLE, whiteSpace: 'nowrap'}}>
                    {t('coins')}
                </Table.Th>}
                <Table.Td style={TABLE_STYLE}>
                    <Group
                        wrap="nowrap"
                        grow
                        gap="xs"
                        className={!showItems ? 'no-print' : undefined}
                    >
                        {reversedEntries.map((entry, index) => {
                            const coinIndex = totalEntriesCount - index;

                            return <TableNumberInput
                                key={entry.currency}
                                miw={0}
                                flex={1}
                                value={localCoins[coinIndex] ?? 0}
                                onChange={n => handleLocalChange(coinIndex, Number(n))}
                                onBlur={handleBlur}
                                rightSection={entry.currencyShortForm}
                                readOnly={!allowEdit}
                                styles={{
                                    root: {minWidth: 0},
                                    wrapper: {minWidth: 0},
                                    input: {minWidth: 0, width: '100%'},
                                }}
                            />;
                        })}
                        <TableNumberInput
                            miw={0}
                            flex={1}
                            value={localCoins[0] ?? 0}
                            onChange={n => handleLocalChange(0, Number(n))}
                            onBlur={handleBlur}
                            rightSection={currencySettings.baseCurrencyShortForm ?? currencySettings.baseCurrency}
                            readOnly={!allowEdit}
                            styles={{
                                root: {minWidth: 0},
                                wrapper: {minWidth: 0},
                                input: {minWidth: 0, width: '100%'},
                            }}
                        />
                    </Group>
                </Table.Td>
            </Table.Tr>
        </Table.Tbody>
    </Table>;
}

function CurrencyMultiLine({
    withoutLabel,
    showCurrency,
    localCoins,
    handleLocalChange,
    handleBlur
}: {
    withoutLabel: boolean;
    showCurrency: string;
    localCoins: number[];
    handleBlur: () => void;
    handleLocalChange: (index: number, newValue: number) => void;
}) {
    const {currencySettings} = useUniverseContext();
    const {allowEdit} = useContext(PnPCharacterContext);
    const {showItems} = useContext(PnPCharacterPrintContext);


    if (showCurrency === SHOW_ALL_CURRENCIES) {
        return <Table variant="vertical" withTableBorder layout="fixed">
            <Table.Tbody>
                <Table.Tr h={TABLE_ROW_HEIGHT}>
                    {withoutLabel ? null : <Table.Th style={TABLE_STYLE}>{currencySettings.baseCurrency}</Table.Th>}
                    <Table.Td style={TABLE_STYLE}>
                        <TableNumberInput
                            value={localCoins[0] ?? ''}
                            onChange={n => handleLocalChange(0, Number(n))}
                            onBlur={handleBlur}
                            rightSection={withoutLabel ? currencySettings.baseCurrencyShortForm : null}
                            readOnly={!allowEdit}
                            className={!showItems ? 'no-print' : undefined}
                        />
                    </Table.Td>
                </Table.Tr>
                {currencySettings.calculationEntries.map((entry, index) => {
                    const coinIndex = index + 1;
                    return (
                        <Table.Tr h={TABLE_ROW_HEIGHT} key={index}>
                            {withoutLabel ? null : <Table.Th style={TABLE_STYLE}>{entry.currency}</Table.Th>}
                            <Table.Td style={TABLE_STYLE}>
                                <TableNumberInput
                                    value={localCoins[coinIndex] ?? ''}
                                    onChange={n => handleLocalChange(coinIndex, Number(n))}
                                    onBlur={handleBlur}
                                    rightSection={withoutLabel ? entry.currencyShortForm : null}
                                    readOnly={!allowEdit}
                                    className={!showItems ? 'no-print' : undefined}
                                />
                            </Table.Td>
                        </Table.Tr>
                    );
                })}
            </Table.Tbody>
        </Table>;
    }
    if (showCurrency === currencySettings.baseCurrency) {
        return <Table
            variant="vertical"
            layout="fixed"
            withTableBorder
        >
            <Table.Tbody>
                <Table.Tr h={TABLE_ROW_HEIGHT}>
                    {withoutLabel ? null : <Table.Th style={TABLE_STYLE}>{currencySettings.baseCurrency}</Table.Th>}
                    <TableNumberInput
                        value={localCoins[0] ?? ''}
                        onChange={n => handleLocalChange(0, Number(n))}
                        onBlur={handleBlur}
                        rightSection={withoutLabel ? currencySettings.baseCurrencyShortForm : null}
                        readOnly={!allowEdit}
                        className={!showItems ? 'no-print' : undefined}
                    />
                </Table.Tr>
            </Table.Tbody>
        </Table>;
    }

    return <Table
        variant="vertical"
        withTableBorder
        layout="fixed"
    >
        <Table.Tbody>
            {currencySettings.calculationEntries.map((entry, index) => {
                if (entry.currency !== showCurrency) {
                    return null;
                }
                const coinIndex = index + 1;
                return (
                    <Table.Tr h={TABLE_ROW_HEIGHT} key={index}>
                        {withoutLabel ? null : <Table.Th style={TABLE_STYLE}>{entry.currency}</Table.Th>}
                        <Table.Td style={TABLE_STYLE}>
                            <TableNumberInput
                                value={localCoins[coinIndex] ?? ''}
                                onChange={n => handleLocalChange(coinIndex, Number(n))}
                                onBlur={handleBlur}
                                rightSection={withoutLabel ? entry.currencyShortForm : null}
                                readOnly={!allowEdit}
                            />
                        </Table.Td>
                    </Table.Tr>
                );
            })}
        </Table.Tbody>
    </Table>;
}

function CurrencyPartSettings({
    withoutLabel,
    oneLine,
    showCurrency,
    setWithoutLabel,
    setOneLine,
    setShowCurrency
}: {
    withoutLabel: boolean;
    oneLine: boolean;
    showCurrency: string;
    setWithoutLabel: (b: boolean) => void;
    setOneLine: (b: boolean) => void;
    setShowCurrency: (s: string) => void;
}) {
    const {t} = useTranslation();
    const {currencySettings} = useUniverseContext();

    const data = useMemo(() => {
        const result = [{
            value: SHOW_ALL_CURRENCIES,
            label: t('sheetEditor:showAllCurrencies')
        }];
        if (!currencySettings) {
            return result;
        }
        result.push({
            value: currencySettings.baseCurrency,
            label: currencySettings.baseCurrency
        });
        currencySettings.calculationEntries.forEach(e => result.push({
            value: e.currency,
            label: e.currency
        }));
        return result;
    }, [currencySettings]);

    return <Stack>
        <Switch
            label={t('sheetEditor:withoutLabel')}
            checked={withoutLabel}
            onChange={e => setWithoutLabel(e.target.checked)}
        />
        <Switch
            label={t('sheetEditor:oneLine')}
            checked={oneLine}
            onChange={e => setOneLine(e.target.checked)}
        />
        <Select
            data={data}
            value={showCurrency}
            onChange={d => setShowCurrency(d)}
            disabled={oneLine}
        />
    </Stack>;
}
