import { Alert, AlertDescription, AlertIcon, AlertTitle, Box, Button, Flex, Heading, Icon, Spinner } from '@chakra-ui/react'
import React, { useState } from 'react'
import { PiNumberCircleTwoFill } from 'react-icons/pi'

enum ExecutionError {
    COMPILATION_ERROR = 'Compilation Error',
    RUNTIME_ERROR = 'Runtime Error',
    UNKNOWN_ERROR = 'Unknown Error',
}

type ExecutorProps = {
    handleResponse: (response: JSON) => void
}

export default function Executor({ handleResponse }: ExecutorProps) {
    const [isExecuting, setIsExecuting] = useState<boolean>(false)
    const [error, setError] = useState<null | ExecutionError>(null)

    let errorMessage =
        error === ExecutionError.COMPILATION_ERROR
            ? 'An error occurred during compilation. Please fix all compilation errors and try again.'
            : error === ExecutionError.RUNTIME_ERROR
              ? 'An error occurred while running your program. Please fix all runtime errors and try again'
              : 'An unknown error occurred. Please try again'
    const handleSubmit = async () => {
        setIsExecuting(true)
        setError(null)
        const url = 'http://localhost:8080/execute'
        const results = await fetch(url, {
            method: 'POST',
        })
            .then((res) => {
                setIsExecuting(false)
                if (res.ok) return res.json()
                switch (res.status) {
                    case 400:
                        setError(ExecutionError.COMPILATION_ERROR)
                        break
                    case 418:
                        setError(ExecutionError.RUNTIME_ERROR)
                        break
                    default:
                        setError(ExecutionError.UNKNOWN_ERROR)
                        break
                }
            })
            .catch((err) => {
                setIsExecuting(false)
                setError(ExecutionError.UNKNOWN_ERROR)
                console.log('An error occurred while calling /execute: ', err)
            })
        handleResponse(results)
    }

    return (
        <Flex
            gap={2}
            p={3}
            borderRadius="10px"
            border="1px solid #B2BABB"
            flexDir="column"
        >
            <Flex gap={1}>
                <Icon
                    as={PiNumberCircleTwoFill}
                    boxSize={6}
                />
                <Heading size="md">Execute your program</Heading>
            </Flex>
            <Button
                width="100%"
                onClick={handleSubmit}
                colorScheme="green"
            >
                Execute!
            </Button>
            {isExecuting && (
                <Box
                    display="flex"
                    justifyContent="center"
                >
                    <Spinner />
                </Box>
            )}
            {error !== null && (
                <Alert status="error">
                    <AlertIcon />
                    {/* <AlertTitle>{error}</AlertTitle> */}
                    <AlertDescription>{errorMessage}</AlertDescription>
                </Alert>
            )}
        </Flex>
    )
}
