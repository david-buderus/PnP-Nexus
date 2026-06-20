import {List, TextInput, TextInputProps, Tooltip} from '@mantine/core';
import {useTranslation} from 'react-i18next';

/**
 * An input for a formular
 */
export default function FormularInput(props: {
    supportedVariables: string[];
    supportedFunctions: string[];
} & TextInputProps) {
    const {t} = useTranslation();
    const {supportedVariables, supportedFunctions, ...other} = props;

    return <Tooltip label={
        <>
            {t('character:calculationFormulaTooltip')}
            <List>
                {supportedVariables.map(v =>
                    <List.Item key={'tooltip-supported-variables-' + v}>
                        {v}
                    </List.Item>)
                }
            </List>
            {t('character:calculationFormulaFunctionTooltip')}
            <List>
                {supportedFunctions.map(f =>
                    <List.Item key={'tooltip-supported-function-' + f}>
                        {f}
                    </List.Item>)
                }
            </List>
        </>}
    >
        <TextInput {...other}/>
    </Tooltip>;
}