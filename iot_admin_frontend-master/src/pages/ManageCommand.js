import { Upload, message, Button } from 'antd';
import { InboxOutlined, PoweroffOutlined } from '@ant-design/icons';
import React from 'react';

const { Dragger } = Upload;

const props = {
    name: 'file',
    accept: ".xlsx",
    multiple: true,
    action: `${process.env.REACT_APP_API_ENDPOINT}/device/command/upload`,
    method: "POST",
    headers: {
      "Authorization": localStorage.getItem("accessToken")
    },
    onChange(info) {
        const { status } = info.file;
        if (status !== 'uploading') {
            console.log(info.file, info.fileList);
        }
        if (status === 'done') {
            message.success(`${info.file.name} file uploaded successfully.`);
        } else if (status === 'error') {
            if (info.file.error.status === 403) {
                localStorage.removeItem("accessToken")
                window.location.reload();
            }
            message.error(`${info.file.name} file upload failed.`);
        }
    },
    onDrop(e) {
        console.log(123)
        //   console.log('Dropped files', e.dataTransfer.files);
    },
};

export default class ManageCommand extends React.Component {

    constructor(props) {
        super(props)

        this.state = {
            loadings: []
        }
    }

    enterLoading = index => {
        this.setState(({ loadings }) => {
            const newLoadings = [...loadings];
            newLoadings[index] = true;

            return {
                loadings: newLoadings,
            };
        });
        var myHeaders = new Headers();
        myHeaders.append("Content-Type", "application/json");
        myHeaders.append("Authorization", localStorage.getItem("accessToken"));

        var requestOptions = {
            method: 'PUT',
            headers: myHeaders,
            redirect: 'follow',
        };
        fetch(`${process.env.REACT_APP_API_ENDPOINT}/device/command/reset`, requestOptions)
            .then(response => {
                if (response.status === 200)
                    return response.json()
                else if (response.status === 401) {
                    localStorage.removeItem("accessToken")
                    window.location.reload();
                }
            })
            .then(data => {
                this.setState(({ loadings }) => {
                    const newLoadings = [...loadings];
                    newLoadings[index] = false;

                    return {
                        loadings: newLoadings,
                    };
                });
            });
    };

    render() {
        const { loadings } = this.state;
        return <div style={{ height: '70vh' }}>
            <div style={{ width: "100%", display: "flex", justifyContent: "center", paddingTop: "2%" }}>
                <h1 style={{}}>Tải lên tệp cấu hình các nút điều khiển</h1>
            </div>
            <div style={{ display: 'flex', justifyContent: 'center', width: '100%', paddingTop: "2%" }}>
                <Dragger {...props} style={{ width: "100%" }}>
                    <p className="ant-upload-drag-icon">
                        <InboxOutlined />
                    </p>
                    <p className="ant-upload-text">Chọn hoặc kéo tệp vào đây</p>
                    <p className="ant-upload-hint">
                        Chọn tệp mong muốn tải lên hệ thống. Lưu ý, tệp cần được viết đúng theo mẫu đã chỉ định từ trước.
                    </p>
                </Dragger>
            </div>
            <div style={{ width: "100%", display: "flex", justifyContent: "center", paddingTop: "6%" }}>
                <h1 style={{}}>Hoàn tác trở lại cấu hình mặc định của hệ thống</h1>
            </div>
            <div style={{ width: "100%", height: '30%', display: "flex", justifyContent: "center", paddingTop: "2%" }}>
                <Button
                    style={{ width: "14%", height: "50%" }}
                    type="primary"
                    icon={<PoweroffOutlined />}
                    loading={loadings[1]}
                    onClick={() => this.enterLoading(1)}
                >
                    Đặt lại
                </Button>
            </div>
        </div>
    }
}