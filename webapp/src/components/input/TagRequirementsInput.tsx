import {ActionIcon, Box, Button, Group, Input, Stack, TagsInput, Text, Tooltip} from '@mantine/core';
import {TagRequirement} from '../../api';
import {FaRegTrashCan} from 'react-icons/fa6';
import {fetchAllTags} from '../Database';
import {MdOutlineHelpOutline} from 'react-icons/md';

/** Props of the tag requirement input */
interface TagRequirementsInputProps {
    /** The current value */
    value?: TagRequirement;
    /** The change callback */
    onChange?: (t: TagRequirement) => void;
    /** The text of the tooltip */
    tooltip: string;
    /** The label used for everything */
    label: string;
    /** The text shown if there are no requirements */
    noRequirementsText: string;
    /** The Text of the button which adds an requirement */
    addTagRequirementText: string;
    /** Prefix added before all data-testids */
    dataTestIdPrefix?: string;
}

/** Input to change tag requirements */
export default function TagRequirementsInput({
    value, onChange, tooltip, label, noRequirementsText, addTagRequirementText, dataTestIdPrefix = ''
}: TagRequirementsInputProps) {
    const [tags] = fetchAllTags();

    return <Stack gap={3}>
        <Tooltip label={tooltip}>
            <Group gap={1}>
                <Input.Label>
                    {label}
                </Input.Label>
                <MdOutlineHelpOutline/>
            </Group>
        </Tooltip>
        {value.tagRequirements.length === 0 ?
            <Text>{noRequirementsText}</Text>
            : null}
        {value.tagRequirements.map((requirement, index) =>
            <Group key={'requirement-' + index} wrap="nowrap">
                <Box
                    style={{flex: 1}}
                >
                    <TagsInput
                        value={(requirement)}
                        data-testid={dataTestIdPrefix + 'tagRequirements-field-' + index}
                        onChange={(newReq) => onChange({
                            tagRequirements: value.tagRequirements.map((v, i) => i !== index ? v : newReq)
                        })}
                        data={tags}
                        clearable
                    />
                </Box>
                <ActionIcon
                    variant="outline"
                    color="red"
                    size="input-sm"
                    data-testid={dataTestIdPrefix + 'tagRequirements-sub-' + index}
                    onClick={() => onChange({
                        tagRequirements: value.tagRequirements.filter((_, i) => i !== index)
                    })}>
                    <FaRegTrashCan/>
                </ActionIcon>
            </Group>
        )}
        <Button
            data-testid={dataTestIdPrefix + 'tagRequirements-add'}
            onClick={() => onChange({
                tagRequirements: value.tagRequirements.concat([[]])
            })}
        >
            {addTagRequirementText}
        </Button>
    </Stack>;
}