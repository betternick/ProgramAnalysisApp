import { RadioGroup, Stack, Radio, Flex, Heading, Icon } from '@chakra-ui/react'
import React, { useEffect, useState } from 'react'
import { PiNumberCircleThreeFill } from 'react-icons/pi'

type StatPickerProps = {
    handleStatChange: (stat: string) => void
}

export default function StatPicker({ handleStatChange }: StatPickerProps) {
    const [value, setValue] = useState<string>('executionTimesDeviation')
    useEffect(() => {
        handleStatChange(value)
    }, [value])

    return (
        <Flex
            gap={2}
            p={3}
            mt={1}
            borderRadius="10px"
            border="1px solid #B2BABB"
            flexDir="column"
        >
            <Flex gap={1}>
                <Icon
                    as={PiNumberCircleThreeFill}
                    boxSize={6}
                />
                <Heading size="md">Pick the stat you wish to compare</Heading>
            </Flex>
            <RadioGroup
                onChange={setValue}
                value={value}
            >
                <Stack
                    direction="column"
                    ml={2}
                >
                    <Radio
                        size="sm"
                        value="executionTimesDeviation"
                    >
                        Number of Times Executed
                    </Radio>
                    <Radio
                        size="sm"
                        value="averageExecutionTimeDeviation"
                    >
                        Average Execution Time
                    </Radio>
                    <Radio
                        size="sm"
                        value="averageMemoryUsageDeviation"
                    >
                        Average Memory Usage
                    </Radio>
                    <Radio
                        size="sm"
                        value="averageCpuUsageDeviation"
                    >
                        Average CPU Usage
                    </Radio>
                </Stack>
            </RadioGroup>
        </Flex>
    )
}
