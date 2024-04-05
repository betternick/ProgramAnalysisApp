import { Button, Flex, Heading, Icon, Text } from '@chakra-ui/react'
import React from 'react'
import { PiNumberCircleTwoFill } from 'react-icons/pi'

export default function Executor() {
    const handleSubmit = async () => {
        const url = 'http://localhost:8080/execute'
        const results = await fetch(url, {
            method: 'POST',
        })
            .then((res) => {
                if (res.ok) {
                    return res.json()
                } else {
                    console.log(res)
                }
            })
            .catch((err) => console.log('An error occurred while calling /execute: ', err))
        console.log(results)
    }

    return (
        <Flex
            gap={2}
            p={3}
            borderRadius="10px"
            border="1px solid #e8e8e8"
            flexDir="column"
        >
            <Flex gap={1}>
                <Icon
                    as={PiNumberCircleTwoFill}
                    boxSize={6}
                />
                <Heading size="md">Excecute your code</Heading>
            </Flex>
            <Button
                width="100%"
                onClick={handleSubmit}
                colorScheme="green"
            >
                Execute!
            </Button>
        </Flex>
    )
}
