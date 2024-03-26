import {Box, Flex, Text} from '@chakra-ui/react'
import React, {useCallback} from 'react'
import {FileRejection, useDropzone} from 'react-dropzone'

type DropzoneProps = {
    dropHandler: (accepted: Array<File>, rejected: Array<FileRejection>) => void
    text?: string
    padding?: number
}

const Dropzone = ({dropHandler, text, padding}: DropzoneProps) => {
    const onDrop = useCallback(dropHandler, [dropHandler])
    const {getRootProps, getInputProps, isDragActive} = useDropzone({
        onDrop,
        maxFiles: 1,
    })

    return (
        <Box
            p={padding ? padding : 6}
            bg="gray.50"
            borderColor="gray.200"
            borderWidth={1}
            borderRadius="lg"
            borderStyle="dashed"
            _hover={{borderColor: 'gray.400'}}
            {...getRootProps()}
        >
            <input {...getInputProps({webkitdirectory: 'true'})} />
            {isDragActive ? (
                <p>
                    Drop your <em>.java</em> file here...
                </p>
            ) : (
                <Flex
                    flexDirection="column"
                    justifyContent="center"
                    alignItems="center"
                >
                    <Text fontStyle="italic">{text ? text : 'Drag & drop your .java file here, or click or to select'}</Text>
                </Flex>
            )}
        </Box>
    )
}

export default Dropzone
