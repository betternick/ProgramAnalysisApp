import React, {useState} from 'react'
import {PiNumberCircleOneFill} from 'react-icons/pi'
import {Tooltip, Button, Box, Flex, Code, CloseButton, Spinner, Heading, Text, Alert, AlertIcon, AlertDescription, Icon, AlertTitle} from '@chakra-ui/react'
import Dropzone from './Dropzone'
import {FileRejection} from 'react-dropzone'

declare module 'react' {
    interface InputHTMLAttributes<T> extends HTMLAttributes<T> {
        webkitdirectory?: string
    }
}

const Uploader = () => {
    const [file, setFile] = useState<File | null>(null)
    const [isUploading, setIsUploading] = useState<boolean>(false)
    const [error, setError] = useState<string | null>(null)

    const errorMessage = () => {
        switch (error) {
            case 'too-many-files':
                return 'Too many files selected. Please upload a singular .java file'
            case 'wrong-file-type':
                return 'Wrong file type. Please upload a singular .java file'
            case 'payload-too-large':
                return 'Max file size exceeded. Please upload a smaller .java file'
            case 'upload-error':
                return "An error occurred while uploading your file. Please try again'"
            default:
                return 'An unknown error occurred. Please try again'
        }
    }

    const handleDrop = (accepted: Array<File>, rejected: Array<FileRejection>) => {
        if (rejected.length > 0) {
            setError(rejected[0].errors[0].code)
            setFile(null)
        } else if (!accepted[0].name.endsWith('.java')) {
            setError('wrong-file-type')
            setFile(null)
        } else {
            setFile(accepted[0])
            setError(null)
        }
    }

    const handleRemove = () => {
        setFile(null)
    }

    const handleSubmit = async () => {
        if (!file) return
        const fd = new FormData()
        fd.append(`file`, file)
        const url = 'http://localhost:8080/upload'
        setIsUploading(true)
        const results = await fetch(url, {
            method: 'POST',
            body: fd,
        })
            .then((res) => {
                if (res.ok) {
                    setFile(null)
                    setIsUploading(false)
                    setError(null)
                    return res.json()
                } else {
                    setError('upload-error')
                }
            })
            .catch((err) => {
                setIsUploading(false)
                if (err instanceof TypeError) {
                    // TODO: change if other errors come up
                    // right now, assumes payload is too large
                    setError('payload-too-large')
                } else {
                    setError(err)
                    console.log(err)
                }
            })
        console.log(results)
    }

    return (
        <Flex
            minW="60vw"
            mt={2}
            borderWidth={1}
            borderRadius="lg"
            p={6}
            direction="column"
            gap={2}
        >
            <Flex
                alignItems="center"
                gap={1}
            >
                <Icon
                    as={PiNumberCircleOneFill}
                    boxSize={6}
                />
                <Heading size="md">Upload your program</Heading>
            </Flex>
            <Text>
                Please upload your program as a single <Code>.java</Code> file
            </Text>
            <Box>
                {file ? (
                    <Flex
                        direction="column"
                        gap={1}
                    >
                        <Flex>
                            <Text
                                fontWeight="bold"
                                mr={3}
                            >
                                Selected File:{' '}
                            </Text>
                            <Flex>
                                {file.name}
                                <Tooltip
                                    aria-label="Remove file"
                                    label="Remove file"
                                    placement="auto"
                                >
                                    <CloseButton
                                        aria-label="Remove file"
                                        size="sm"
                                        mr={1}
                                        onClick={handleRemove}
                                    />
                                </Tooltip>
                            </Flex>
                        </Flex>
                        <Box>
                            <Dropzone
                                dropHandler={handleDrop}
                                text={'Drag or click to select a different file'}
                                padding={3}
                            />
                        </Box>
                    </Flex>
                ) : (
                    <Box>
                        <Dropzone dropHandler={handleDrop} />
                    </Box>
                )}
            </Box>
            <Button
                onClick={handleSubmit}
                colorScheme="teal"
            >
                Submit
            </Button>
            {isUploading && (
                <Box
                    display="flex"
                    justifyContent="center"
                >
                    <Spinner />
                </Box>
            )}
            {error && (
                <Alert status="error">
                    <AlertIcon />
                    <AlertTitle>Error</AlertTitle>
                    <AlertDescription>{errorMessage()}</AlertDescription>
                </Alert>
            )}
        </Flex>
    )
}

export default Uploader
