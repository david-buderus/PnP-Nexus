import {useNode} from '@craftjs/core';
import {Select, Stack, Switch, Table} from '@mantine/core';
import {getPartStyle, TABLE_ROW_HEIGHT, TABLE_STYLE} from '../Constants';
import React, {useContext, useMemo} from 'react';
import {useTranslation} from 'react-i18next';
import {PnPCharacterContext} from '../../../PnPCharacterContext';
import {currencyFormatter} from '../../../../utils/Formatters';
import {useUniverseContext} from '../../../../PageBase';
import {splitCurrency} from '../../../../utils/Utils';

/** Constant to show all currencies */
export const SHOW_ALL_CURRENCIES = '__ALL_CURRENCIES__';

/** Shows the currency of the character */
export const CurrencyPart = ({withoutLabel, oneLine, showCurrency}: {
    withoutLabel: boolean;
    oneLine: boolean;
    showCurrency: string;
}) => {
    if (oneLine) {
        return <CurrencyOneLine withoutLabel={withoutLabel}/>;
    }
    return <CurrencyMultiLine withoutLabel={withoutLabel} showCurrency={showCurrency}/>;
};

function CurrencyOneLine({withoutLabel}: {
    withoutLabel: boolean;
}) {
    const {t} = useTranslation();
    const {connectors: {connect, drag}, selected} = useNode((state => ({
        selected: state.events.selected
    })));
    const {currencySettings} = useUniverseContext();
    const {character} = useContext(PnPCharacterContext);
    const result = useMemo(() => currencyFormatter(currencySettings, character.inventory.coin), [character.inventory.coin]);

    return <Table
        variant="vertical"
        layout="fixed"
        withTableBorder
        ref={ref => connect(drag(ref))}
        style={getPartStyle(selected)}
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
    const {connectors: {connect, drag}, selected} = useNode((state => ({
        selected: state.events.selected
    })));
    const {currencySettings} = useUniverseContext();
    const {character} = useContext(PnPCharacterContext);
    const coins = useMemo(() => splitCurrency(currencySettings, character.inventory.coin),
        [currencySettings, character.inventory.coin]);

    if (showCurrency === SHOW_ALL_CURRENCIES) {
        return <Table
            variant="vertical"
            layout="fixed"
            withTableBorder
            ref={ref => connect(drag(ref))}
            style={getPartStyle(selected)}
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
            ref={ref => connect(drag(ref))}
            style={getPartStyle(selected)}
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
        ref={ref => connect(drag(ref))}
        style={getPartStyle(selected)}
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

const CurrencyPartSettings = () => {
    const {t} = useTranslation();
    const {currencySettings} = useUniverseContext();
    const {actions: {setProp}, withoutLabel, oneLine, showCurrency} = useNode(node => ({
        withoutLabel: node.data.props.withoutLabel,
        oneLine: node.data.props.oneLine,
        showCurrency: node.data.props.showCurrency
    }));
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
            value={withoutLabel}
            onChange={e => setProp(props => {
                props.withoutLabel = e.target.checked;
            })}
        />
        <Switch
            label={t('sheetEditor:oneLine')}
            value={oneLine}
            onChange={e => setProp(props => {
                props.oneLine = e.target.checked;
            })}
        />
        <Select
            data={data}
            value={showCurrency}
            onChange={d => {
                setProp(props => {
                    props.showCurrency = d;
                });
            }}
            disabled={oneLine}
        />
    </Stack>;
};

CurrencyPart.craft = {
    name: 'sheetEditor:currency',
    related: {
        settings: CurrencyPartSettings
    }
};