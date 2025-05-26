import {ActionIcon, Button, Group, Table, TextInput} from "@mantine/core";
import {randomId} from "@mantine/hooks";
import {ReactNode, useEffect} from "react";
import {FaRegTrashCan} from "react-icons/fa6";
import {handleNetworkErrors, handleValidationErrors} from "../utils/ErrorUtils";
import {useTranslation} from "react-i18next";
import {useUniverseContext} from "../PageBase";
import {PrimaryAttribute, PrimaryAttributeServiceApi} from "../../api";
import {useForm} from "@mantine/form";
import {API_CONFIGURATION} from "../Constants";

const PRIMARY_ATTRIBUTE_API = new PrimaryAttributeServiceApi(API_CONFIGURATION);

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
        <Table>
            <Table.Thead>
                <Table.Tr>
                    <Table.Th>{t("name")}</Table.Th>
                    <Table.Th>{t("character:shortName")}</Table.Th>
                    <Table.Th></Table.Th>
                </Table.Tr>
            </Table.Thead>
            <Table.Tbody>
                {form.getValues().attributes.map((attribute, index) => {
                    return <Table.Tr key={attribute.key}>
                        <Table.Td>
                            <TextInput
                                key={form.key(`attributes.${index}.name`)}
                                required
                                {...form.getInputProps(`attributes.${index}.name`)}
                            />
                        </Table.Td>
                        <Table.Td>
                            <TextInput
                                key={form.key(`attributes.${index}.shortName`)}
                                required
                                {...form.getInputProps(`attributes.${index}.shortName`)}
                            />
                        </Table.Td>
                        <Table.Td>
                            <ActionIcon variant="outline" size="lg" color="red"
                                        onClick={() => form.removeListItem('attributes', index)}>
                                <FaRegTrashCan size={16}/>
                            </ActionIcon>
                        </Table.Td>
                    </Table.Tr>;
                })}
            </Table.Tbody>
            {form.getValues().attributes.length === 0 ?
                <Table.Caption c="dimmed" ta="center">
                    {t("nothing-here")}
                </Table.Caption> : null}
        </Table>
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