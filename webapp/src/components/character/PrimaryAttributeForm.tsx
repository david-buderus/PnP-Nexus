import {ActionIcon, Button, Grid, Group, Text, TextInput} from "@mantine/core";
import {randomId} from "@mantine/hooks";
import {ReactNode, useEffect} from "react";
import {FaRegTrashCan} from "react-icons/fa6";
import {PRIMARY_ATTRIBUTE_API} from "../../pages/universe/universe-creation";
import {handleNetworkErrors, handleValidationErrors} from "../utils/ErrorUtils";
import {useTranslation} from "react-i18next";
import {useUniverseContext} from "../PageBase";
import {PrimaryAttribute} from "../../api";
import {useForm} from "@mantine/form";


/** A form to adjust all primary attributes */
export function PrimaryAttributeForm({
    onSave, onSaveText, alternativeButton
}: {
    onSave: () => void;
    onSaveText: string;
    alternativeButton?: ReactNode;
}) {
    const {t} = useTranslation();
    const {activeUniverse} = useUniverseContext();

    const form = useForm<{
        attributes: (PrimaryAttribute & { key: string; })[];
    }>({
        mode: 'uncontrolled',
        initialValues: {
            attributes: Array(8).fill({shortName: "", name: ""}).map(a => {
                return {...a, key: randomId()};
            })
        }
    });

    useEffect(() => {
        if (!activeUniverse) {
            return;
        }
        PRIMARY_ATTRIBUTE_API.getAllPrimaryAttributes(activeUniverse.name).then(response => {
            if (response.data.length > 0) {
                form.setValues({
                    attributes: response.data.map(a => {
                        return {
                            ...a,
                            key: randomId()
                        };
                    })
                });
            }
        }).catch(handleNetworkErrors);
    }, [activeUniverse]);

    return <form
        onSubmit={form.onSubmit((attributes) => PRIMARY_ATTRIBUTE_API.setAllPrimaryAttributes(activeUniverse.name, attributes.attributes)
            .then(onSave).catch(handleValidationErrors(form.setErrors))
        )}>
        <Grid columns={3} justify="center">
            {form.getValues().attributes.length > 0 ? (
                <>
                    <Grid.Col span={1} key="name-label">
                        <Text fw={500} size="sm" style={{flex: 1}}>
                            {t("name")}
                        </Text>
                    </Grid.Col>
                    <Grid.Col span={1} key="shortName-label">
                        <Text fw={500} size="sm" pr={160}>
                            {t("character:shortName")}
                        </Text>
                    </Grid.Col>
                    <Grid.Col span="content" key="button-label">
                        <ActionIcon size="lg" style={{'visibility': 'hidden'}}/>
                    </Grid.Col>
                </>
            ) : (
                <Grid.Col span={3} key="nothing-label">
                    <Text c="dimmed" ta="center">
                        {t("nothing-here")}
                    </Text>
                </Grid.Col>
            )}
            {form.getValues().attributes.flatMap((item, index) => {
                return [
                    <Grid.Col key={item.key + "-name-grid"} span={1}>
                        <TextInput
                            key={form.key(`attributes.${index}.name`)}
                            required
                            {...form.getInputProps(`attributes.${index}.name`)}
                        />
                    </Grid.Col>,
                    <Grid.Col key={item.key + "-shortName-grid"} span={1}>
                        <TextInput
                            key={form.key(`attributes.${index}.shortName`)}
                            required
                            {...form.getInputProps(`attributes.${index}.shortName`)}
                        />
                    </Grid.Col>,
                    <Grid.Col key={item.key + "-button"} span="content">
                        <ActionIcon variant="outline" size="lg" color="red"
                                    onClick={() => form.removeListItem('attributes', index)}>
                            <FaRegTrashCan size={16}/>
                        </ActionIcon>
                    </Grid.Col>
                ];
            })}
        </Grid>
        <Button
            mt="md"
            onClick={() =>
                form.insertListItem('attributes', {name: '', shortName: '', key: randomId()})
            }
        >
            {t("universe:addAnotherAttribute")}
        </Button>

        <Group justify="flex-end" pt="md">
            {alternativeButton}
            <Button type="submit">
                {onSaveText}
            </Button>
        </Group>
    </form>;
}