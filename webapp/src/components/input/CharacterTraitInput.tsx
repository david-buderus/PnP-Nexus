import {PrimaryStatTrait, SecondaryStatTrait, SimpleCharacterTrait, TalentCharacterTrait} from '../../api';
import {ActionIcon, Box, Button, Card, Group, NumberInput, Select, Stack, Textarea} from '@mantine/core';
import {useTranslation} from 'react-i18next';
import {UseFormReturnType} from '@mantine/form';
import {randomId} from '@mantine/hooks';
import {PrimaryAttributeSelect, SecondaryAttributeSelect, TalentSelect} from './ObjectSelect';
import {CalculationSelect} from './EnumSelect';
import {FaRegTrashCan} from 'react-icons/fa6';


/** The different character traits */
export type ICharacterTrait = (PrimaryStatTrait | SecondaryStatTrait | SimpleCharacterTrait | TalentCharacterTrait) & {
    '@type': string;
    key?: string;
};

/** The props for the input */
interface CharacterTraitInputProps {
    /** The form which controls the underlying object */
    form: UseFormReturnType<any, (values: any) => any>;
    /** Path to the list of character traits */
    path: string;
}

/**
 * An input to create a list of character traits.
 */
export function CharacterTraitInput({
    form,
    path
}: CharacterTraitInputProps) {
    const {t} = useTranslation();

    const values: ICharacterTrait[] = form.getValues()[path];

    return <Stack w={400}>
        {values.map((trait, index) => {
            if (!trait.key) {
                trait.key = randomId();
            }

            return <Card shadow="sm" key={trait.key}>
                <Stack gap="xs">
                    <Group wrap="nowrap">
                        <Box
                            style={{flex: 1}}
                        >
                            <Select
                                data={[
                                    {
                                        value: 'SimpleCharacterTrait',
                                        label: t('character:simpleTrait')
                                    },
                                    {
                                        value: 'TalentCharacterTrait',
                                        label: t('character:talentTrait')
                                    },
                                    {
                                        value: 'PrimaryStatTrait',
                                        label: t('character:primaryAttributeTrait')
                                    },
                                    {
                                        value: 'SecondaryStatTrait',
                                        label: t('character:secondaryAttributeTrait')
                                    }
                                ]}
                                key={form.key(`${path}.${index}.@type`)}
                                {...form.getInputProps(`${path}.${index}.@type`)}
                            />
                        </Box>
                        <ActionIcon
                            variant="outline"
                            color="red"
                            size="lg"
                            data-testid={path + '-sub-' + index}
                            onClick={() => form.removeListItem(path, index)}
                        >
                            <FaRegTrashCan size={16}/>
                        </ActionIcon>
                    </Group>
                    <Textarea
                        label={t('description')}
                        key={form.key(`${path}.${index}.description`)}
                        {...form.getInputProps(`${path}.${index}.description`)}
                    />
                    {trait['@type'] === 'TalentCharacterTrait' ?
                        <Group wrap="nowrap" align="flex-start">
                            <NumberInput
                                key={form.key(`${path}.${index}.rollModifier`)}
                                {...form.getInputProps(`${path}.${index}.rollModifier`)}
                                label={t('value')}
                            />
                            <TalentSelect
                                key={form.key(`${path}.${index}.talent`)}
                                {...form.getInputProps(`${path}.${index}.talent`)}
                                label={t('talent')}
                            />
                        </Group> : null
                    }
                    {trait['@type'] === 'PrimaryStatTrait' || trait['@type'] === 'SecondaryStatTrait' ?
                        <Group wrap="nowrap" align="flex-start">
                            <CalculationSelect
                                key={form.key(`${path}.${index}.calculation`)}
                                {...form.getInputProps(`${path}.${index}.calculation`)}
                            />
                            <NumberInput
                                key={form.key(`${path}.${index}.value`)}
                                {...form.getInputProps(`${path}.${index}.value`)}
                                label={t('value')}
                            />
                        </Group> : null
                    }
                    {trait['@type'] === 'PrimaryStatTrait' ?
                        <PrimaryAttributeSelect
                            key={form.key(`${path}.${index}.attribute`)}
                            {...form.getInputProps(`${path}.${index}.attribute`)}
                            label={t('primary-attribute')}
                        /> : null}
                    {trait['@type'] === 'SecondaryStatTrait' ?
                        <SecondaryAttributeSelect
                            key={form.key(`${path}.${index}.attribute`)}
                            {...form.getInputProps(`${path}.${index}.attribute`)}
                            label={t('secondary-attribute')}
                        /> : null}
                </Stack>
            </Card>;
        })}
        {values.length === 0 ? t('nothing-here') : null}
        <Group justify="flex-end">
            <Button
                data-testid={path + '-add'}
                onClick={() =>
                    form.insertListItem(path, {'@type': 'SimpleCharacterTrait', description: '', key: randomId()})
                }
            >
                {t('add')}
            </Button>
        </Group>
    </Stack>;
}