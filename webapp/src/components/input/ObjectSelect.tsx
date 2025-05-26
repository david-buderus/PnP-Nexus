import {ActionIcon, Group, Input, MultiSelect, MultiSelectProps, Select, SelectProps} from '@mantine/core';
import {SomeItem} from '../Constants';
import {
    fetchAllItems,
    fetchAllMaterials,
    fetchAllPrimaryAttributes,
    fetchAllSecondaryAttributes,
    fetchAllTalents,
    IResource
} from '../Database';
import {MdRefresh} from 'react-icons/md';
import {useTranslation} from 'react-i18next';
import {useMemo} from 'react';
import {PrimaryAttribute, SecondaryAttribute, Talent} from '../../api';

/** Props for the select */
export interface ObjectSelectProps<O> extends Omit<SelectProps, "data" | "onChange" | "value"> {
    /** The data of the select */
    data: O[];
    /** The current value of the select */
    value?: O;
    /** The change callback of the select */
    onChange?: (o: O) => void;
    /** The key to get the id */
    idKey: keyof O;
    /** The key to get the label */
    labelKey: keyof O;
}

/** Props for the multiselect */
export interface ObjectMultiSelectProps<O> extends Omit<MultiSelectProps, "data" | "onChange" | "value"> {
    /** The data of the select */
    data: O[];
    /** The current value of the select */
    value?: O[];
    /** The change callback of the select */
    onChange?: (o: O[]) => void;
    /** The key to get the id */
    idKey: keyof O;
    /** The key to get the label */
    labelKey: keyof O;
}

/** Select for complex objects */
export function ObjectSelect<O>(props: ObjectSelectProps<O>) {
    const {data, value, onChange, idKey, labelKey, ...rest} = props;

    return <Select
        data={data.map(o => {
            return {
                value: o[idKey] as string,
                label: o[labelKey] as string,
            };
        })}
        value={value?.[idKey] as string}
        onChange={v => onChange(data.find(o => o[idKey] === v))}
        searchable
        {...rest}
    />;
}

/** Multi select for complex objects */
export function ObjectMultiSelect<O>(props: ObjectMultiSelectProps<O>) {
    const {data, value, onChange, idKey, labelKey, ...rest} = props;

    return <MultiSelect
        data={data.map(o => {
            return {
                value: o[idKey] as string,
                label: o[labelKey] as string
            };
        })}
        value={value?.map(v => v[idKey] as string)}
        onChange={v => onChange(v.map(id => data.find(o => o[idKey] === id)))}
        searchable
        {...rest}
    />;
}

/** Props for the select */
export interface ObjectSelectWithRefreshProps<O> extends Omit<SelectProps, "data" | "onChange" | "value"> {
    /** The data of the select */
    data: O[];
    /** Refreshed the loaded data */
    refresh: () => void;
    /** If the data gets currently loaded */
    loading: boolean;
    /** The current value of the select */
    value?: O;
    /** The change callback of the select */
    onChange?: (o: O) => void;
    /** The key to get the id */
    idKey: keyof O;
    /** The key to get the label */
    labelKey: keyof O;
}


function ObjectSelectWithRefresh<O>({
    data, refresh, loading, idKey, labelKey, label, error, description, required, size = "sm", ...props
}: ObjectSelectWithRefreshProps<O>) {
    return <Input.Wrapper
        label={label}
        error={error}
        description={description}
        required={required}
    >
        <Group wrap="nowrap" gap={1}>
            <ObjectSelect
                data={data}
                idKey={idKey}
                labelKey={labelKey}
                size={size}
                error={!!error}
                {...props}
            />
            <ActionIcon
                onClick={refresh}
                loading={loading}
                variant="outline"
                size={"input-" + size}
            >
                <MdRefresh/>
            </ActionIcon>
        </Group>
    </Input.Wrapper>;
}

/** Select over all items of a universe */
export function ItemSelect(props: Omit<ObjectSelectProps<SomeItem>, "data" | "idKey" | "labelKey">) {
    const [items, refresh, loading] = fetchAllItems();

    return <ObjectSelectWithRefresh
        data={items}
        refresh={refresh}
        loading={loading}
        idKey="id"
        labelKey="name"
        {...props}
    />;
}

/** Select over all talent of a universe */
export function TalentSelect(props: Omit<ObjectSelectProps<Talent>, "data" | "idKey" | "labelKey">) {
    const [talents, refresh, loading] = fetchAllTalents();

    return <ObjectSelectWithRefresh
        data={talents}
        refresh={refresh}
        loading={loading}
        idKey="id"
        labelKey="name"
        {...props}
    />;
}

/** Select over all primary attributes of a universe */
export function PrimaryAttributeSelect(props: Omit<ObjectSelectProps<PrimaryAttribute>, "data" | "idKey" | "labelKey">) {
    const [attributes, refresh, loading] = fetchAllPrimaryAttributes();

    return <ObjectSelectWithRefresh
        data={attributes}
        refresh={refresh}
        loading={loading}
        idKey="id"
        labelKey="name"
        {...props}
    />;
}

/** Select over all secondary attributes of a universe */
export function SecondaryAttributeSelect(props: Omit<ObjectSelectProps<SecondaryAttribute>, "data" | "idKey" | "labelKey">) {
    const [attributes, refresh, loading] = fetchAllSecondaryAttributes();

    return <ObjectSelectWithRefresh
        data={attributes}
        refresh={refresh}
        loading={loading}
        idKey="id"
        labelKey="name"
        {...props}
    />;
}

/** Select over all resources of a universe */
export function ResourceSelect({
    value,
    onChange,
    label,
    error,
    size = "sm",
    ...rest
}: Omit<ObjectSelectProps<IResource>, "data" | "idKey" | "labelKey">) {
    const {t} = useTranslation();
    const [items, refreshItems, loadingItems] = fetchAllItems();
    const [materials, refreshMaterials, loadingMaterials] = fetchAllMaterials();
    const [allAttributes, refreshAttributes, loadingAttributes] = fetchAllSecondaryAttributes();

    const attributes: SecondaryAttribute[] = useMemo(() => {
        return allAttributes.filter(attribute => attribute.consumable);
    }, [allAttributes]);

    const data: IResource[] = useMemo(() => {
        return [].concat(items).concat(materials).concat(attributes);
    }, [items, materials, attributes]);

    const loading: boolean = useMemo(() => {
        return loadingItems || loadingMaterials || loadingAttributes;
    }, [loadingItems, loadingMaterials, loadingAttributes]);

    return <Input.Wrapper
        label={label}
        error={error}
    >
        <Group wrap="nowrap" gap={1} align="flex-start">
            <Select
                data={[
                    {
                        group: t("items"),
                        items: items.map(item => {
                            return {
                                value: item.id,
                                label: item.name
                            };
                        })
                    },
                    {
                        group: t("materials"),
                        items: materials.map(material => {
                            return {
                                value: material.id,
                                label: material.name
                            };
                        })
                    },
                    {
                        group: t("attributes"),
                        items: attributes.map(attribute => {
                            return {
                                value: attribute.id,
                                label: attribute.name
                            };
                        })
                    }
                ]}
                value={value?.id as string}
                onChange={v => onChange(data.find(o => o.id === v))}
                searchable
                size={size}
                error={!!error}
                {...rest}
            />
            <ActionIcon
                onClick={() => {
                    refreshItems();
                    refreshMaterials();
                    refreshAttributes();
                }}
                loading={loading}
                size={"input-" + size}
                variant="outline"
            >
                <MdRefresh/>
            </ActionIcon>
        </Group>
    </Input.Wrapper>;
}