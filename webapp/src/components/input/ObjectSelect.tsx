import {ActionIcon, Group, MultiSelect, MultiSelectProps, Select, SelectProps} from '@mantine/core';
import {SomeItem} from '../Constants';
import {fetchAllItems, fetchAllMaterials, fetchAllSecondaryAttributes, IResource} from '../Database';
import {MdRefresh} from 'react-icons/md';
import {useTranslation} from 'react-i18next';
import {useMemo} from 'react';
import {SecondaryAttribute} from '../../api';

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

/** Select over all items of a universe */
export function ItemSelect(props: Omit<ObjectSelectProps<SomeItem>, "data" | "idKey" | "labelKey">) {
    const [items, refresh, loading] = fetchAllItems();

    return <Group wrap='nowrap' gap={1} align="flex-start">
        <ObjectSelect
            data={items}
            idKey="id"
            labelKey="name"
            {...props}
        />
        <ActionIcon
            onClick={refresh}
            loading={loading}
            size="input-sm"
            variant='outline'
        >
            <MdRefresh/>
        </ActionIcon>
    </Group>;
}

/** Select over all resources of a universe */
export function ResourceSelect({
    value,
    onChange,
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

    return <Group wrap='nowrap' gap={1} align="flex-start">
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
            {...rest}
        />
        <ActionIcon
            onClick={() => {
                refreshItems();
                refreshMaterials();
                refreshAttributes();
            }}
            loading={loading}
            size="input-sm"
            variant='outline'
        >
            <MdRefresh/>
        </ActionIcon>
    </Group>;
}