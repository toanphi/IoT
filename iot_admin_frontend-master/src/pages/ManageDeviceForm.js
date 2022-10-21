import React from 'react';
import { Form, Input, Button, Select } from 'antd';
const { Option } = Select;

class ManageDeviceForm extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            // model
            model: this.props.modifyRecord === false ? {
                controlTopic: "",
                controlType: "wireless",
                desc: "",
                model: "",
                name: "",
                outputTopic: "",
                productType: "",
                remoteType: "tv",
                gangs: ""
            } : this.props.modifyRecord,
            pointerEvent: this.props.modifyRecord === false ? false : true,
            lstSupportedDevice: [],
        }
    }

    componentDidMount(){
        this.fetchSupportedDevice();
    }

    formRef = React.createRef();

    validateMessages = {
        // eslint-disable-next-line
        "required": "${label} cần không thể trống"
    };

    mapProductType = {
        "cảm biến" : "sensor",
        "công tắc": "switch",
        "tivi": "tivi",
        "quạt": "fan",
        "điều hòa": "air-conditioner"
    }
    lstProductTypes = ['cảm biến', 'công tắc', 'tivi', 'quạt', 'điều hòa']


    // form finish
    onFinish = (values) => {
        var device = values.device

        device.status = true

        var name = device.name
        name = name.toLowerCase()
        device.name = name
        var productType = this.mapProductType[values.device.productType]
        device.productType = productType
        var currentTime = new Date().toLocaleString('sv', { timeZoneName: 'short' });
        var strDate = currentTime.substring(0, currentTime.lastIndexOf(" "))
        device.createdDate = strDate;

        this.props.fetchSaveDevice(device)
        this.props.clearModifyRecord()
    };

    /**
     *  productType item onChange
     * @param {*} value 
     */
    onProductTypeChange = (value) => {
        var newModel = Object.assign({}, this.state.model)
        newModel.productType = this.mapProductType[value]
        console.log(newModel.productType)
        this.setState({
            model: newModel
        })
    }

    fetchSupportedDevice = () => {
        var myHeaders = new Headers();
        myHeaders.append("Content-Type", "application/json");
        myHeaders.append("Authorization", localStorage.getItem("accessToken"));

        var requestOptions = {
        method: 'GET',
        headers: myHeaders,
        redirect: 'follow'
        };
        fetch(`${process.env.REACT_APP_API_ENDPOINT}/device/supported/get/all`, requestOptions)
            .then(response => {
                if (response.status === 200) 
                    return response.json()
                else if (response.status === 401) {
                    localStorage.removeItem("accessToken")
                    window.location.reload();
                } 
            })
            .then(data => {
                this.setState({
                    lstSupportedDevice: data.data
                }) 
            })
            .catch(error => console.log('error', error));
    }


    render() {
        return (
            <Form
                ref={this.formRef}
                layout='vertical'
                labelCol={{ span: 24, offset: 0 }}
                wrapperCol={{ span: 24, offset: 0 }}
                name="nest-messages"
                onFinish={this.onFinish}
                validateMessages={this.validateMessages}
                style={{ border: '1px solid #777', width: '30%', margin: 'auto', padding: '20px', borderRadius: '5px', minWidth: '420px', background: 'white', zIndex: 3 }}
            >
                <Form.Item
                    name={['device', 'name']}
                    label="Tên thiết bị"
                    tooltip="Đây là tên thiết bị. Nên đặt tên theo quy tắc: Loại thiết bị + vị trí để đảm bảo điều khiển dễ dàng hơn. Ví dụ: Đèn phòng ngủ"
                    required
                    rules={[
                        { required: true },
                        () => ({
                            validator(_, value) {
                                if (value.match("^[a-zA-Z0-9dđàáãạảăắằẳẵặâấầẩẫậèéẹẻẽêềếểễệđìíĩỉịòóõọỏôốồổỗộơớờởỡợùúũụủưứừửữựỳỵỷỹýDĐÀÁÃẠẢĂẮẰẲẴẶÂẤẦẨẪẬÈÉẸẺẼÊỀẾỂỄỆĐÌÍĨỈỊÒÓÕỌỎÔỐỒỔỖỘƠỚỜỞỠỢÙÚŨỤỦƯỨỪỬỮỰỲỴỶỸÝ ]+$")) {
                                    return Promise.resolve()
                                }

                                return Promise.reject("Tên không thể chứa ký tự đặc biệt")
                            }
                        })
                    ]}
                    initialValue={this.state.model.name}
                >
                    <Input
                        allowClear
                        disabled={this.state.pointerEvent}
                        style={{ minWidth: '250px' }}
                    />
                </Form.Item>
                <Form.Item name={['device', 'productType']} label="Loại thiết bị" tooltip="Loại thiết bị" initialValue={this.state.model.productType}>
                    <Select
                        placeholder="Product Type"
                        style={{ width: 120 }}
                        disabled={this.state.pointerEvent}
                        onChange={this.onProductTypeChange}
                        value={this.state.productType}
                    >
                        {this.lstProductTypes.map(key => (
                            <Option value={key} key={key}>{key}</Option>
                        ))}
                    </Select>
                </Form.Item>

                {this.state.model.productType === "sensor" || this.state.model.productType === "switch" ?
                    <div>
                        <Form.Item name={['device', 'controlTopic']} rules={[{ required: true }]} label="Control Topic" tooltip="Đây là topic để điều khiển thiết bị" initialValue={this.state.model.controlTopic}>
                            <Input allowClear style={{ minWidth: '250px' }} />
                        </Form.Item>
                        <Form.Item name={['device', 'outputTopic']} label="Output Topic" tooltip="Topic dành cho kết quả điều khiển" initialValue={this.state.model.outputTopic}>
                            <Input allowClear />
                        </Form.Item>
                    </div>
                    :
                    <Form.Item
                        name={['device', 'model']}
                        label="Nguyên mẫu"
                        required tooltip={"Các mẫu thiết bị điều khiển hồng ngoại"}
                        initialValue={this.state.model.model}
                        rules={[{ required: true }]}
                    >
                        <Select
                            showSearch
                            optionFilterProp="children"
                            filterOption={(input, option) =>
                                option.children.toLowerCase().indexOf(input.toLowerCase()) >= 0
                            }
                            onChange={this.onIdChange}
                        >
                            {this.state.lstSupportedDevice.map(value => (
                                <Select.Option value={value.name} key={value.name}>{value.name}</Select.Option>
                            ))}
                        </Select>
                    </Form.Item>
                }
                {this.state.model.productType === "switch" ?
                    <Form.Item name={['device', 'gangs']}
                        rules={[
                            { required: true },
                            () => ({
                                validator(_, value) {
                                    if (isNaN(value)) {
                                        return Promise.reject("Bắt buộc phải là số");
                                    }
                                    return Promise.resolve();
                                }
                            })
                        ]}
                        label="Số công tắc"
                        tooltip="Số công tắc thiết bị được gắn vào" initialValue={this.state.model.gangs}>
                        <Input allowClear />
                    </Form.Item> : ""
                }
                <Form.Item name={['device', 'desc']} label="Mô tả" initialValue={this.state.model.desc}>
                    <Input.TextArea allowClear style={{ minWidth: '250px' }} />
                </Form.Item>
                <Form.Item >
                    <div style={{ display: 'flex', justifyContent: 'center' }}>
                        <Button type="default" htmlType="button" onClick={(e) => {
                            this.props.onClose(e);
                            this.props.clearModifyRecord()
                        }}>
                            Hủy
                        </Button>
                        <Button type="primary" htmlType="submit" style={{ marginLeft: '10px' }}>
                            Lưu
                        </Button>
                    </div>

                </Form.Item>
            </Form>
        );
    }
}

export default ManageDeviceForm;
