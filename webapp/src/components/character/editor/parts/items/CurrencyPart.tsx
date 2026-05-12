import {Select, Stack, Switch, Table} from '@mantine/core';
import {TABLE_ROW_HEIGHT, TABLE_STYLE} from '../Constants';
import React, {useContext, useMemo} from 'react';
import {useTranslation} from 'react-i18next';
import {PnPCharacterContext} from '../../../PnPCharacterContext';
import {currencyFormatter} from '../../../../utils/Formatters';
import {useUniverseContext} from '../../../../PageBase';
import {splitCurrency} from '../../../../utils/Utils';
import {PageElementSettings} from '../PageElementSettings';

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
    return <>
        {oneLine ?
            <CurrencyOneLine withoutLabel={withoutLabel}/>
            :
            <CurrencyMultiLine
                withoutLabel={withoutLabel}
                showCurrency={showCurrency}
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

function CurrencyOneLine({withoutLabel}: {
    withoutLabel: boolean;
}) {
    const {t} = useTranslation();
    const {currencySettings} = useUniverseContext();
    const {characterForm} = useContext(PnPCharacterContext);
    const character = characterForm.getValues();
    const result = useMemo(() => currencyFormatter(currencySettings, character.inventory.coin), [character.inventory.coin]);

    return <Table
        variant="vertical"
        layout="fixed"
        withTableBorder
    >
        <Table.Tbody>
            <Table.Tr h={TABLE_ROW_HEIGHT}>
                {withoutLabel ? null : <Table.Th style={TABLE_STYLE}>{t('coins')}</Table.Th>}
                <Table.Td style={TABLE_STYLE}>{result}</Table.Td>
            </Table.Tr>
        </Table.Tbody>
    </Table>;
}

function CurrencyMultiLine({withoutLabel, showCurrency}: {
    withoutLabel: boolean;
    showCurrency: string;
}) {
    const {currencySettings} = useUniverseContext();
    const {characterForm} = useContext(PnPCharacterContext);
    const character = characterForm.getValues();
    const coins = useMemo(() => splitCurrency(currencySettings, character.inventory.coin),
        [currencySettings, character.inventory.coin]);

    if (showCurrency === SHOW_ALL_CURRENCIES) {
        return <Table
            variant="vertical"
            layout="fixed"
            withTableBorder
        >
            <Table.Tbody>
                <Table.Tr h={TABLE_ROW_HEIGHT}>
                    {withoutLabel ? null : <Table.Th style={TABLE_STYLE}>{currencySettings.baseCurrency}</Table.Th>}
                    <Table.Td style={TABLE_STYLE}>
                        {String(coins[0]) + (withoutLabel ? currencySettings.baseCurrencyShortForm : '')}
                    </Table.Td>
                </Table.Tr>
                {currencySettings.calculationEntries.map((entry, index) => (
                    <Table.Tr h={TABLE_ROW_HEIGHT} key={index}>
                        {withoutLabel ? null : <Table.Th style={TABLE_STYLE}>{entry.currency}</Table.Th>}
                        <Table.Td style={TABLE_STYLE}>
                            {String(coins[index + 1]) + (withoutLabel ? entry.currencyShortForm : '')}
                        </Table.Td>
                    </Table.Tr>
                ))}
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
                    <Table.Td style={TABLE_STYLE}>
                        {String(coins[0]) + (withoutLabel ? currencySettings.baseCurrencyShortForm : '')}
                    </Table.Td>
                </Table.Tr>
            </Table.Tbody>
        </Table>;
    }

    return <Table
        variant="vertical"
        layout="fixed"
        withTableBorder
    >
        <Table.Tbody>
            {currencySettings.calculationEntries.map((entry, index) => {
                if (entry.currency !== showCurrency) {
                    return null;
                }
                return (
                    <Table.Tr h={TABLE_ROW_HEIGHT} key={index}>
                        {withoutLabel ? null : <Table.Th style={TABLE_STYLE}>{entry.currency}</Table.Th>}
                        <Table.Td style={TABLE_STYLE}>
                            {String(coins[index + 1]) + (withoutLabel ? entry.currencyShortForm : '')}
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
